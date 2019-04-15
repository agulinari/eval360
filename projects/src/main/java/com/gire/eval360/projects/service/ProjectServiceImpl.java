package com.gire.eval360.projects.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gire.eval360.projects.domain.EvaluationStatus;
import com.gire.eval360.projects.domain.Evaluee;
import com.gire.eval360.projects.domain.EvalueeFeedbackProvider;
import com.gire.eval360.projects.domain.EvalueeReviewer;
import com.gire.eval360.projects.domain.FeedbackProvider;
import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.ProjectAdmin;
import com.gire.eval360.projects.domain.Relationship;
import com.gire.eval360.projects.domain.Reviewer;
import com.gire.eval360.projects.domain.Status;
import com.gire.eval360.projects.domain.dto.AdminStatus;
import com.gire.eval360.projects.domain.dto.CompletedEvaluee;
import com.gire.eval360.projects.domain.dto.EvalueeDetail;
import com.gire.eval360.projects.domain.dto.EvalueeStatus;
import com.gire.eval360.projects.domain.dto.FeedbackProviderDetail;
import com.gire.eval360.projects.domain.dto.FeedbackProviderStatus;
import com.gire.eval360.projects.domain.dto.PendingEvaluee;
import com.gire.eval360.projects.domain.dto.ProjectStatus;
import com.gire.eval360.projects.domain.dto.ProjectStatus.ProjectStatusBuilder;
import com.gire.eval360.projects.domain.dto.ReviewerStatus;
import com.gire.eval360.projects.domain.excel.ProjectAdminExcel;
import com.gire.eval360.projects.domain.excel.ProjectEvalueeExcel;
import com.gire.eval360.projects.domain.excel.ProjectExcel;
import com.gire.eval360.projects.domain.excel.ProjectFpExcel;
import com.gire.eval360.projects.domain.excel.ProjectReviewerExcel;
import com.gire.eval360.projects.domain.history.EvaluationInstance;
import com.gire.eval360.projects.domain.history.UserHistory;
import com.gire.eval360.projects.domain.notifications.NotificationFeedbackProviderDto;
import com.gire.eval360.projects.domain.notifications.NotificationReviewerDto;
import com.gire.eval360.projects.domain.request.CreateEvaluee;
import com.gire.eval360.projects.domain.request.CreateFeedbackProvider;
import com.gire.eval360.projects.domain.request.CreateProjectAdmin;
import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.domain.request.CreateReviewer;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;
import com.gire.eval360.projects.domain.stats.ActiveProjectStats;
import com.gire.eval360.projects.repository.EvalueeFeedbackProviderRepository;
import com.gire.eval360.projects.repository.ProjectRepository;
import com.gire.eval360.projects.service.remote.TemplateServiceRemote;
import com.gire.eval360.projects.service.remote.UserServiceRemote;
import com.gire.eval360.projects.service.remote.dto.templates.TemplateDto;
import com.gire.eval360.projects.service.remote.dto.users.UserDto;
import com.gire.eval360.projects.service.remote.dto.users.UserListDto;
import com.gire.eval360.projects.service.remote.dto.users.UserResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;

	private final EvalueeFeedbackProviderRepository efpRepository;

	private final NotificationFeedBackSender notificationFeedBackSender;

	private final UserServiceRemote userServiceRemote;

	private final TemplateServiceRemote templateServiceRemote;

	@Autowired
	public ProjectServiceImpl(final ProjectRepository projectRepository,
			final EvalueeFeedbackProviderRepository efpRepository,
			final NotificationFeedBackSender notificationFeedBackSender, final UserServiceRemote userServiceRemote,
			final TemplateServiceRemote templateServiceRemote) {
		this.projectRepository = projectRepository;
		this.efpRepository = efpRepository;
		this.notificationFeedBackSender = notificationFeedBackSender;
		this.userServiceRemote = userServiceRemote;
		this.templateServiceRemote = templateServiceRemote;
	}

	public Collection<Project> getProjects() {
		return projectRepository.findAll();
	}

	@Override
	@Transactional
	public Project createProject(CreateProjectRequest request) {

		Map<Long, FeedbackProvider> mapaFps = new HashMap<>();
		Map<Long, Reviewer> mapaReviewers = new HashMap<>();

		Project project = new Project();
		project.setName(request.getName());
		project.setDescription(request.getDescription());
		project.setIdEvaluationTemplate(request.getIdTemplate());
		project.setStartDate(LocalDate.now());
		project.setStatus(Status.PENDIENTE);

		List<CreateEvaluee> createEvaluees = request.getEvaluees();
		List<Evaluee> evaluees = new LinkedList<Evaluee>();

		createEvaluee(mapaFps, mapaReviewers, evaluees, project, createEvaluees);

		project.setEvaluees(evaluees);
		project.setFeedbackProviders(mapaFps.values());
		project.setReviewers(mapaReviewers.values());

		List<CreateProjectAdmin> createAdmins = request.getAdmins();
		List<ProjectAdmin> projectAdmins = new ArrayList<>();

		createProjectAdmin(project, createAdmins, projectAdmins);

		project.setProjectAdmins(projectAdmins);
		projectRepository.save(project);
		armarFeedBack(project);

		return project;
	}

	@Override
	@Transactional
	public void reportFeedback(ReportFeedbackRequest request) {
		EvalueeFeedbackProvider efp = efpRepository.findByEvalueeAndFeedbackProvider(request.getIdEvaluee(),
				request.getIdFeedbackProvider());
		efp.setStatus(EvaluationStatus.RESUELTO);
		sendNotificationReviewerForFeedbackComplete(efp);
	}

	/**
	 * Evalua si no quedaron feedbacks pendientes. En caso de que no queden
	 * feedbacks pendientes, significa que se ha completado la evaluación y por
	 * lo cual se enviará la notificación al evaluado (quien pasará a ser
	 * reviewer), para que pueda ingresar a la aplicación y ver el resultado de
	 * la evaluación.
	 * 
	 * @param efp
	 */
	private void sendNotificationReviewerForFeedbackComplete(EvalueeFeedbackProvider efp) {
		List<EvalueeFeedbackProvider> providers = efp.getEvaluee().getFeedbackProviders();
		Predicate<EvalueeFeedbackProvider> predicate = e -> e.getStatus().equals(EvaluationStatus.PENDIENTE);
		Optional<EvalueeFeedbackProvider> fpPendiente = providers.stream().filter(predicate).findAny();
		if (!fpPendiente.isPresent()) {
			Evaluee evaluee = efp.getEvaluee();
			Project project = evaluee.getProject();
			sendNotificationEvaluationCompleteToReviewer(0L, evaluee.getId(), project.getId(),
					project.getIdEvaluationTemplate());
		}
	}

	/******* METODOS PRIVADOS *******/

	/**
	 * Arma el dto para enviar la notificación a los provideers, haciendoles
	 * llegar un mail, que se genero un nuevo proyecto de evaluación.
	 * 
	 * @param projectGenerated
	 */
	private void armarFeedBack(Project projectGenerated) {

		for (Evaluee evaluee : projectGenerated.getEvaluees()) {

			for (EvalueeFeedbackProvider evalueeFp : evaluee.getFeedbackProviders()) {
				Long idFp = evalueeFp.getFeedbackProvider().getIdUser();
				Long idEvalueeFP = evalueeFp.getId();
				Long idProject = evaluee.getProject().getId();
				sendFeedBackToFeedbackProviders(idFp, idEvalueeFP, idProject);
			}
		}

	}

	/**
	 * Envia notificación a reviewer
	 * 
	 * @param idUser
	 * @param id
	 */
	private void sendNotificationEvaluationCompleteToReviewer(Long idUser, Long idEvaluee, Long idProject,
			Long idTemplate) {
		NotificationReviewerDto notifyFpDto = new NotificationReviewerDto(idUser, idEvaluee, idProject, idTemplate);
		notificationFeedBackSender.sendNotificationReviewer(notifyFpDto);
	}

	/**
	 * Envia notificación a feedbackProviders
	 * 
	 * @param idEvaluee
	 * @param idEvalueeFp
	 * @param idProject
	 */
	private void sendFeedBackToFeedbackProviders(Long idFp, Long idEvalueeFP, Long idProject) {
		NotificationFeedbackProviderDto notifyFpDto = new NotificationFeedbackProviderDto(idFp, idEvalueeFP, idProject);
		notificationFeedBackSender.sendNotificationFeedBackProvider(notifyFpDto);
	}

	/**
	 * Crea los project admins del proyecto de evaluación.
	 * 
	 * @param project
	 * @param createAdmins
	 * @param projectAdmins
	 */
	private void createProjectAdmin(Project project, List<CreateProjectAdmin> createAdmins,
			List<ProjectAdmin> projectAdmins) {
		for (CreateProjectAdmin createAdmin : createAdmins) {
			ProjectAdmin admin = new ProjectAdmin();
			admin.setIdUser(createAdmin.getIdUser());
			admin.setCreator(createAdmin.getCreator());
			admin.setCreatedDate(LocalDateTime.now());
			admin.setProject(project);
			projectAdmins.add(admin);
		}
	}

	/**
	 * Crea el evaluee y posteriormente llama al metodo de
	 * createFeedBackProvider para asociar los proveedores de feedback para el
	 * evaluado creado.
	 * 
	 * @param mapaFps
	 * @param project
	 * @param createEvaluees
	 */
	private void createEvaluee(Map<Long, FeedbackProvider> mapaFps, Map<Long, Reviewer> mapaReviewers,
			List<Evaluee> evaluees, Project project, List<CreateEvaluee> createEvaluees) {

		for (CreateEvaluee createEvaluee : createEvaluees) {
			Evaluee evaluee = new Evaluee();
			evaluee.setIdUser(createEvaluee.getIdUser());
			evaluee.setProject(project);
			createFeedBackProvider(mapaFps, project, createEvaluee, evaluee);
			createReviewer(mapaReviewers, project, createEvaluee, evaluee);
			evaluees.add(evaluee);
		}
	}

	/**
	 * Crea los feedbacks providers a partir de los evaluee
	 * 
	 * @param mapaFps
	 * @param project
	 * @param createEvaluee
	 * @param evaluee
	 */
	private void createFeedBackProvider(Map<Long, FeedbackProvider> mapaFps, Project project,
			CreateEvaluee createEvaluee, Evaluee evaluee) {

		for (CreateFeedbackProvider createFp : createEvaluee.getFeedbackProviders()) {
			EvalueeFeedbackProvider efp = new EvalueeFeedbackProvider();
			efp.setStatus(EvaluationStatus.PENDIENTE);
			efp.setEvaluee(evaluee);

			FeedbackProvider fp = mapaFps.get(createFp.getIdUser());
			if (fp == null) {
				fp = new FeedbackProvider();
				fp.setIdUser(createFp.getIdUser());
				fp.setProject(project);
				fp.getEvaluees().add(efp);
				mapaFps.put(createFp.getIdUser(), fp);
			}

			efp.setFeedbackProvider(fp);
			efp.setRelationship(createFp.getRelationship());
			evaluee.getFeedbackProviders().add(efp);
		}
	}

	/**
	 * Crea los feedbacks providers a partir de los evaluee
	 * 
	 * @param mapaFps
	 * @param project
	 * @param createEvaluee
	 * @param evaluee
	 */
	private void createReviewer(Map<Long, Reviewer> mapaReviewers, Project project, CreateEvaluee createEvaluee,
			Evaluee evaluee) {

		for (CreateReviewer createReviewer : createEvaluee.getReviewers()) {
			EvalueeReviewer er = new EvalueeReviewer();
			er.setEvaluee(evaluee);

			Reviewer reviewer = mapaReviewers.get(createReviewer.getIdUser());
			if (reviewer == null) {
				reviewer = new Reviewer();
				reviewer.setIdUser(createReviewer.getIdUser());
				reviewer.setProject(project);
				reviewer.getEvaluees().add(er);
				mapaReviewers.put(createReviewer.getIdUser(), reviewer);
			}

			er.setReviewer(reviewer);
			evaluee.getReviewers().add(er);
		}
	}

	@Override
	@Transactional
	public void addEvaluee(Long id, CreateEvaluee createEvaluee) {

		Optional<Project> project = projectRepository.findById(id);

		project.ifPresent(p -> {

			Map<Long, FeedbackProvider> mapaFps = new HashMap<>();
			Map<Long, Reviewer> mapaReviewers = new HashMap<>();

			for (FeedbackProvider fp : p.getFeedbackProviders()) {
				mapaFps.put(fp.getIdUser(), fp);
			}

			for (Reviewer reviewer : p.getReviewers()) {
				mapaReviewers.put(reviewer.getIdUser(), reviewer);
			}

			Evaluee evaluee = new Evaluee();
			evaluee.setIdUser(createEvaluee.getIdUser());
			evaluee.setProject(p);

			for (CreateFeedbackProvider createFp : createEvaluee.getFeedbackProviders()) {
				EvalueeFeedbackProvider efp = new EvalueeFeedbackProvider();
				efp.setStatus(EvaluationStatus.PENDIENTE);
				efp.setEvaluee(evaluee);

				FeedbackProvider fp = mapaFps.get(createFp.getIdUser());
				if (fp == null) {
					fp = new FeedbackProvider();
					fp.setIdUser(createFp.getIdUser());
					fp.setProject(p);
					fp.getEvaluees().add(efp);
					mapaFps.put(createFp.getIdUser(), fp);
				}

				efp.setFeedbackProvider(fp);
				efp.setRelationship(createFp.getRelationship());
				evaluee.getFeedbackProviders().add(efp);
				sendFeedBackToFeedbackProviders(fp.getIdUser(), efp.getId(), evaluee.getProject().getId());
			}

			for (CreateReviewer createReviewer : createEvaluee.getReviewers()) {
				EvalueeReviewer er = new EvalueeReviewer();
				er.setEvaluee(evaluee);

				Reviewer reviewer = mapaReviewers.get(createReviewer.getIdUser());
				if (reviewer == null) {
					reviewer = new Reviewer();
					reviewer.setIdUser(createReviewer.getIdUser());
					reviewer.setProject(p);
					reviewer.getEvaluees().add(er);
					mapaReviewers.put(createReviewer.getIdUser(), reviewer);
				}

				er.setReviewer(reviewer);
				evaluee.getReviewers().add(er);
			}

			p.getEvaluees().add(evaluee);

		});

	}

	@Override
	@Transactional
	public void addAdmin(Long id, CreateProjectAdmin createAdmin) {
		Optional<Project> project = projectRepository.findById(id);

		project.ifPresent(p -> {

			ProjectAdmin admin = new ProjectAdmin();
			admin.setIdUser(createAdmin.getIdUser());
			admin.setProject(p);
			admin.setCreator(createAdmin.getCreator());

			p.getProjectAdmins().add(admin);
		});
	}

	@Override
	public List<Evaluee> getEvalueesForFeedback(Long id, Long idFp) {
		Optional<Project> project = projectRepository.findById(id);

		List<Evaluee> evaluees = new ArrayList<>();
		project.ifPresent(p -> {
			Optional<FeedbackProvider> feedbackProvider = p.getFeedbackProviders().stream()
					.filter(fp -> fp.getIdUser().equals(idFp)).findFirst();
			feedbackProvider.ifPresent(fp -> {
				for (EvalueeFeedbackProvider efp : fp.getEvaluees()) {
					if (efp.getStatus().equals(EvaluationStatus.PENDIENTE)) {
						Evaluee evaluee = efp.getEvaluee();
						evaluees.add(evaluee);
					}
				}
			});
		});
		return evaluees;
	}

	@Override
	public Optional<ProjectStatus> getProjectStatus(Long id) {

		Optional<Project> project = projectRepository.findById(id);

		return project.map(p -> buildProjectStatus(p));

	}

	private ProjectStatus buildProjectStatus(Project p) {
		ProjectStatusBuilder builder = ProjectStatus.builder().id(p.getId()).name(p.getName()).idTemplate(p.getIdEvaluationTemplate())
				.description(p.getDescription());

		List<EvalueeStatus> evalueesStatus = p.getEvaluees().stream().map(e -> buildEvalueeStatus(e))
				.collect(Collectors.toList());
		List<FeedbackProviderStatus> fpStatus = p.getFeedbackProviders().stream()
				.map(fp -> buildFeedbackProviderStatus(fp)).collect(Collectors.toList());
		List<ReviewerStatus> reviewerStatus = p.getReviewers().stream().map(r -> buildReviewerStatus(r))
				.collect(Collectors.toList());
		List<AdminStatus> adminStatus = p.getProjectAdmins().stream().map(a -> buildAdminStatus(a))
				.collect(Collectors.toList());

		ProjectStatus projectStatus = builder.evalueesStatus(evalueesStatus).feedbackProvidersStatus(fpStatus)
				.reviewersStatus(reviewerStatus).adminsStatus(adminStatus).build();
		return projectStatus;
	}

	private EvalueeStatus buildEvalueeStatus(Evaluee e) {
		UserResponse user = this.userServiceRemote.getUserById(e.getIdUser());

		EvalueeStatus evalueeStatus = EvalueeStatus.builder().id(e.getId()).idUser(e.getIdUser())
				.username(user.getUsername()).avatar("").status(Status.PENDIENTE).completedFeedbacks(0)
				.pendingFeedbacks(0).build();

		List<FeedbackProviderDetail> fpDetails = e.getFeedbackProviders().stream()
				.map(efp -> buildFeedbackProviderDetail(efp)).collect(Collectors.toList());
		Integer totalFeedbacks = fpDetails.size();
		Integer completedFeedbacks = 0;
		for (FeedbackProviderDetail fpDetail : fpDetails) {
			if (fpDetail.getStatus().equals(EvaluationStatus.RESUELTO)) {
				completedFeedbacks++;
			}
		}
		Integer pendingFeedbacks = totalFeedbacks - completedFeedbacks;

		evalueeStatus.setPendingFeedbacks(pendingFeedbacks);
		evalueeStatus.setCompletedFeedbacks(completedFeedbacks);
		evalueeStatus.setFeedbackProviders(fpDetails);
		if (completedFeedbacks == totalFeedbacks) {
			evalueeStatus.setStatus(Status.RESUELTO);
		}
		return evalueeStatus;
	}

	private FeedbackProviderDetail buildFeedbackProviderDetail(EvalueeFeedbackProvider efp) {

		UserResponse user = this.userServiceRemote.getUserById(efp.getFeedbackProvider().getIdUser());

		FeedbackProviderDetail fpDetail = FeedbackProviderDetail.builder().id(efp.getFeedbackProvider().getId())
				.avatar("").idUser(efp.getFeedbackProvider().getIdUser()).username(user.getUsername())
				.status(efp.getStatus()).build();

		return fpDetail;
	}

	private FeedbackProviderStatus buildFeedbackProviderStatus(FeedbackProvider fp) {
		UserResponse user = this.userServiceRemote.getUserById(fp.getIdUser());

		FeedbackProviderStatus fpStatus = FeedbackProviderStatus.builder().id(fp.getId()).idUser(fp.getIdUser())
				.username(user.getUsername()).avatar("").status(Status.PENDIENTE).completedFeedbacks(0)
				.pendingFeedbacks(0).build();

		List<EvalueeDetail> evalueeDetails = fp.getEvaluees().stream().map(efp -> buildEvalueeDetail(efp))
				.collect(Collectors.toList());
		Integer totalFeedbacks = evalueeDetails.size();
		Integer completedFeedbacks = 0;
		for (EvalueeDetail evalueeDetail : evalueeDetails) {
			if (evalueeDetail.getStatus().equals(EvaluationStatus.RESUELTO)) {
				completedFeedbacks++;
			}
		}
		Integer pendingFeedbacks = totalFeedbacks - completedFeedbacks;

		fpStatus.setPendingFeedbacks(pendingFeedbacks);
		fpStatus.setCompletedFeedbacks(completedFeedbacks);
		fpStatus.setEvaluees(evalueeDetails);
		if (completedFeedbacks == totalFeedbacks) {
			fpStatus.setStatus(Status.RESUELTO);
		}
		return fpStatus;
	}

	private EvalueeDetail buildEvalueeDetail(EvalueeFeedbackProvider efp) {
		UserResponse user = this.userServiceRemote.getUserById(efp.getEvaluee().getIdUser());

		EvalueeDetail evalueeDetail = EvalueeDetail.builder().id(efp.getEvaluee().getId()).avatar("")
				.idUser(efp.getEvaluee().getIdUser()).username(user.getUsername()).status(efp.getStatus())
				.relationship(efp.getRelationship()).build();

		return evalueeDetail;
	}

	private ReviewerStatus buildReviewerStatus(Reviewer r) {
		UserResponse user = this.userServiceRemote.getUserById(r.getIdUser());

		ReviewerStatus reviewerStatus = ReviewerStatus.builder().id(r.getId()).idUser(r.getIdUser())
				.username(user.getUsername()).avatar("").status(Status.PENDIENTE).completedReports(0).pendingReports(0)
				.build();

		List<EvalueeDetail> evalueeDetails = r.getEvaluees().stream().map(er -> buildEvalueeReviewerDetail(er))
				.collect(Collectors.toList());

		reviewerStatus.setEvaluees(evalueeDetails);

		return reviewerStatus;
	}

	private EvalueeDetail buildEvalueeReviewerDetail(EvalueeReviewer er) {
		UserResponse user = this.userServiceRemote.getUserById(er.getEvaluee().getIdUser());

		EvalueeDetail evalueeDetail = EvalueeDetail.builder().id(er.getEvaluee().getId()).avatar("")
				.idUser(er.getEvaluee().getIdUser()).username(user.getUsername()).build();

		return evalueeDetail;
	}

	private AdminStatus buildAdminStatus(ProjectAdmin a) {
		UserResponse user = this.userServiceRemote.getUserById(a.getIdUser());

		AdminStatus adminStatus = AdminStatus.builder().id(a.getId()).idUser(a.getIdUser()).username(user.getUsername())
				.avatar("").creator(a.getCreator()).build();

		return adminStatus;
	}

	@Override
	public List<PendingEvaluee> getPendingEvalueesForUser(Long id, Long idUser) {
		Optional<Project> oProject = projectRepository.findById(id);

		List<PendingEvaluee> pendingEvaluees = new ArrayList<>();
		if (oProject.isPresent()) {
			Project project = oProject.get();
			for (Evaluee evaluee : project.getEvaluees()) {
				for (EvalueeFeedbackProvider efp : evaluee.getFeedbackProviders()) {
					if ((efp.getFeedbackProvider().getIdUser().longValue() == idUser)
							&& (efp.getStatus().equals(EvaluationStatus.PENDIENTE))) {
						PendingEvaluee pendingEvaluee = buildPendingEvaluee(evaluee, efp.getId(),
								efp.getRelationship());
						pendingEvaluees.add(pendingEvaluee);
					}
				}
			}
			return pendingEvaluees;
		} else {
			log.warn("Proyecto con id {} inexistente", id);
			return pendingEvaluees;
		}
	}

	private PendingEvaluee buildPendingEvaluee(Evaluee evaluee, Long efpId, Relationship relationship) {
		UserResponse user = this.userServiceRemote.getUserById(evaluee.getIdUser());

		PendingEvaluee pendingEvaluee = PendingEvaluee.builder().id(evaluee.getId()).idEvalueeFp(efpId)
				.idUser(evaluee.getIdUser()).avatar("").relationShip(relationship).username(user.getUsername()).build();
		return pendingEvaluee;
	}

	@Override
	public List<CompletedEvaluee> getCompletedEvalueesForUser(Long id, Long idUser) {
		Optional<Project> oProject = projectRepository.findById(id);

		List<CompletedEvaluee> completedEvaluees = new ArrayList<>();

		if (oProject.isPresent()) {
			Project project = oProject.get();

			for (Evaluee evaluee : project.getEvaluees()) {
				// Filtro los evaluees que tienen como reviewer al usuario
				Optional<EvalueeReviewer> er = evaluee.getReviewers().stream()
						.filter(r -> r.getReviewer().getIdUser().longValue() == idUser).findAny();
				if (er.isPresent()) {
					boolean completed = true;
					for (EvalueeFeedbackProvider efp : evaluee.getFeedbackProviders()) {
						if (efp.getStatus().equals(EvaluationStatus.PENDIENTE)) {
							completed = false;
							break;
						}
					}
					if (completed) {
						CompletedEvaluee completedEvaluee = buildCompletedEvaluee(evaluee);
						completedEvaluees.add(completedEvaluee);
					}
				}

			}
			return completedEvaluees;
		} else {
			log.warn("Proyecto con id {} inexistente", id);
			return completedEvaluees;
		}
	}

	private CompletedEvaluee buildCompletedEvaluee(Evaluee evaluee) {
		UserResponse user = this.userServiceRemote.getUserById(evaluee.getIdUser());

		CompletedEvaluee completedEvaluee = CompletedEvaluee.builder().id(evaluee.getId()).idUser(evaluee.getIdUser())
				.avatar("").username(user.getUsername()).build();
		return completedEvaluee;
	}

	@Override
	public Project importProject(ProjectExcel projectExcel) {

		TemplateDto templateDto = this.templateServiceRemote.getTemplateByName(projectExcel.getTemplate());

		List<String> usernames = this.getUsernames(projectExcel);

		UserListDto usersDto = this.userServiceRemote.getUsersByUsername(usernames);

		CreateProjectRequest createProjectRequest = this.createProjectRequest(projectExcel, templateDto,
				usersDto.get_embedded().getUsers());

		Project project = this.createProject(createProjectRequest);
		return project;
	}

	private CreateProjectRequest createProjectRequest(ProjectExcel projectExcel, TemplateDto templateDto,
			List<UserDto> usersDto) {

		List<CreateProjectAdmin> admins = createProjectAdmins(projectExcel.getAdmins(), usersDto);

		List<CreateEvaluee> evaluees = createProjectEvaluees(projectExcel.getEvaluees(), usersDto);

		CreateProjectRequest createRequest = CreateProjectRequest.builder().idTemplate(templateDto.getId())
				.description(projectExcel.getDescription().trim()).name(projectExcel.getName().trim()).admins(admins)
				.evaluees(evaluees).build();
		return createRequest;
	}

	private List<CreateEvaluee> createProjectEvaluees(List<ProjectEvalueeExcel> evaluees, List<UserDto> usersDto) {

		List<CreateEvaluee> createEvaluees = new ArrayList<>();

		for (ProjectEvalueeExcel evaluee : evaluees) {
			Optional<UserDto> userDto = usersDto.stream()
					.filter(u -> u.getUsername().toUpperCase().equals(evaluee.getUsername())).findAny();

			if (!userDto.isPresent()) {
				throw new IllegalStateException("No existe el usuario del evaluee " + evaluee.getUsername());
			}

			CreateEvaluee createEvaluee = new CreateEvaluee();
			createEvaluee.setIdUser(userDto.get().getId());

			List<CreateFeedbackProvider> feedbackProviders = createProjectFps(evaluee.getFeedbackProviders(), usersDto);
			List<CreateReviewer> reviewers = createProjectReviewers(evaluee.getReviewers(), usersDto);

			if (feedbackProviders.isEmpty()) {
				throw new IllegalStateException("No hay feedback providers para el evaluee " + evaluee.getUsername());
			}

			if (reviewers.isEmpty()) {
				throw new IllegalStateException("No hay reviewers para el evaluee " + evaluee.getUsername());
			}

			createEvaluee.setFeedbackProviders(feedbackProviders);
			createEvaluee.setReviewers(reviewers);
			createEvaluees.add(createEvaluee);
		}

		return createEvaluees;
	}

	private List<CreateReviewer> createProjectReviewers(List<ProjectReviewerExcel> reviewers, List<UserDto> usersDto) {
		List<CreateReviewer> createReviewers = new ArrayList<>();

		for (ProjectReviewerExcel reviewer : reviewers) {
			Optional<UserDto> userDto = usersDto.stream()
					.filter(u -> u.getUsername().toUpperCase().equals(reviewer.getUsername())).findAny();

			if (!userDto.isPresent()) {
				throw new IllegalStateException("No existe el usuario del reviewer" + reviewer.getUsername());
			}

			CreateReviewer createReviewer = new CreateReviewer();
			createReviewer.setIdUser(userDto.get().getId());
			createReviewers.add(createReviewer);
		}

		return createReviewers;
	}

	private List<CreateFeedbackProvider> createProjectFps(List<ProjectFpExcel> feedbackProviders,
			List<UserDto> usersDto) {
		List<CreateFeedbackProvider> createFps = new ArrayList<>();

		for (ProjectFpExcel fp : feedbackProviders) {
			Optional<UserDto> userDto = usersDto.stream()
					.filter(u -> u.getUsername().toUpperCase().equals(fp.getUsername())).findAny();

			if (!userDto.isPresent()) {
				throw new IllegalStateException("No existe el usuario del feedback provider" + fp.getUsername());
			}

			CreateFeedbackProvider createFp = new CreateFeedbackProvider();
			createFp.setIdUser(userDto.get().getId());
			createFp.setRelationship(fp.getRelationship());
			createFps.add(createFp);
		}

		return createFps;
	}

	private List<CreateProjectAdmin> createProjectAdmins(List<ProjectAdminExcel> admins, List<UserDto> usersDto) {

		List<CreateProjectAdmin> createAdmins = new ArrayList<>();
		for (ProjectAdminExcel admin : admins) {
			Optional<UserDto> userDto = usersDto.stream()
					.filter(u -> u.getUsername().toUpperCase().equals(admin.getUsername())).findAny();

			if (!userDto.isPresent()) {
				throw new IllegalStateException("No existe el usuario " + admin.getUsername());
			}

			CreateProjectAdmin createAdmin = CreateProjectAdmin.builder().idUser(userDto.get().getId()).creator(false)
					.build();

			createAdmins.add(createAdmin);
		}
		return createAdmins;
	}

	private List<String> getUsernames(ProjectExcel projectExcel) {

		HashSet<String> usernamesSet = new HashSet<>();

		for (ProjectEvalueeExcel evaluee : projectExcel.getEvaluees()) {
			usernamesSet.add(evaluee.getUsername());
			for (ProjectReviewerExcel reviewer : evaluee.getReviewers()) {
				usernamesSet.add(reviewer.getUsername());
			}
			for (ProjectFpExcel fp : evaluee.getFeedbackProviders()) {
				usernamesSet.add(fp.getUsername());
			}

		}

		for (ProjectAdminExcel admin : projectExcel.getAdmins()) {
			usernamesSet.add(admin.getUsername());
		}

		List<String> usernamesList = new ArrayList<>(usernamesSet);
		return usernamesList;
	}

	@Override
	public UserHistory getUserHistory(Long idUser) {
		List<Project> projects = projectRepository.findEvalueeHistory(idUser);

		List<EvaluationInstance> evalInstances = new ArrayList<>();
		for (Project project : projects) {
			EvaluationInstance evalInstance = createEvaluationInstance(project, idUser);
			evalInstances.add(evalInstance);
		}
		UserHistory userHistory = UserHistory.builder().idUser(idUser).evaluationInstances(evalInstances).build();

		return userHistory;
	}

	private EvaluationInstance createEvaluationInstance(Project project, Long idUser) {

		Optional<Evaluee> evaluee = project.getEvaluees().stream()
				.filter(e -> e.getIdUser().longValue() == idUser.longValue()).findFirst();
		// Siempre existe el evaluee
		List<EvalueeFeedbackProvider> efps = evaluee.get().getFeedbackProviders();

		List<Long> feedbackProvidersIds = efps.stream().map(efp -> efp.getFeedbackProvider().getIdUser())
				.collect(Collectors.toList());

		LocalDate endDate = LocalDate.now();
		if (project.getEndDate() != null) {
			endDate = project.getEndDate();
		}

		EvaluationInstance instance = EvaluationInstance.builder().idProject(project.getId())
				.projectName(project.getName()).startDate(project.getStartDate()).endDate(endDate)
				.feedbackProvidersIds(feedbackProvidersIds).build();
		return instance;
	}

	@Override
	public List<ActiveProjectStats> getActiveProjectsStats() {
		List<Project> projects = projectRepository.findByStatus(Status.PENDIENTE);
		
		List<ActiveProjectStats> stats = projects.stream().map(this::getActiveProjectStats).collect(Collectors.toList());
		return stats;
	}
	
	private ActiveProjectStats getActiveProjectStats(Project p) {
		Integer evalueesCount = p.getEvaluees().size();

		ActiveProjectStats noStats = ActiveProjectStats.builder()
							.evaluationsByManagers(0)
							.evaluationsByPeers(0)
							.evaluationsBySubordiantes(0)
							.build();
		
		
		Optional<ActiveProjectStats> oStats = p.getEvaluees().stream().map(this::getEvalueeStats).reduce(this::acumulateStats);
		
		ActiveProjectStats stats = oStats.orElse(noStats);
		
		stats.setProjectName(p.getName());
		stats.setEvaluees(evalueesCount);
		return stats;
	}

	
	
	private ActiveProjectStats getEvalueeStats(Evaluee e) {
		
		Integer evalByManagers = 0;
		Integer evalByPeers = 0;
		Integer evalBySubordinates = 0;
		
		for (EvalueeFeedbackProvider efp : e.getFeedbackProviders()) {
			if (efp.getRelationship().equals(Relationship.JEFE)) {
				evalByManagers++;
			}
			if (efp.getRelationship().equals(Relationship.PAR)) {
				evalByPeers++;
			}
			if (efp.getRelationship().equals(Relationship.SUBORDINADO)) {
				evalBySubordinates++;
			}
		}
		
		return ActiveProjectStats.builder()
		.evaluationsByManagers(evalByManagers)
		.evaluationsByPeers(evalByPeers)
		.evaluationsBySubordiantes(evalBySubordinates)
		.build();
		
	}
	
	private ActiveProjectStats acumulateStats(ActiveProjectStats s1, ActiveProjectStats s2) {
		return ActiveProjectStats.builder()
			.evaluationsByManagers(s1.getEvaluationsByManagers() + s2.getEvaluationsByManagers())
			.evaluationsByPeers(s1.getEvaluationsByPeers() + s2.getEvaluationsByPeers())
			.evaluationsBySubordiantes(s1.getEvaluationsBySubordiantes() + s2.getEvaluationsBySubordiantes())
			.build();
		
	}

	@Override
	public List<Reviewer> getReviewers(Long id, Long idEvaluee) {
		Optional<Project> oProject = this.projectRepository.findById(id);
		return oProject.map(project -> {
			Optional<Evaluee> oEvaluee = project.getEvaluees().stream().filter(e -> e.getId() == idEvaluee.longValue()).findFirst();
			return oEvaluee.map(evaluee -> evaluee.getReviewers().stream().map(r -> r.getReviewer()).collect(Collectors.toList())).orElse(new ArrayList<>());
		}).orElse(new ArrayList<>());
	}
}
