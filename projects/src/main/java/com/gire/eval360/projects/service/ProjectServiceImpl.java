package com.gire.eval360.projects.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import com.gire.eval360.projects.domain.Reviewer;
import com.gire.eval360.projects.domain.Status;
import com.gire.eval360.projects.domain.dto.AdminStatus;
import com.gire.eval360.projects.domain.dto.EvalueeDetail;
import com.gire.eval360.projects.domain.dto.EvalueeStatus;
import com.gire.eval360.projects.domain.dto.FeedbackProviderDetail;
import com.gire.eval360.projects.domain.dto.FeedbackProviderStatus;
import com.gire.eval360.projects.domain.dto.ProjectStatus;
import com.gire.eval360.projects.domain.dto.ProjectStatus.ProjectStatusBuilder;
import com.gire.eval360.projects.domain.dto.ReviewerStatus;
import com.gire.eval360.projects.domain.notifications.NotificationFeedbackProviderDto;
import com.gire.eval360.projects.domain.notifications.NotificationReviewerDto;
import com.gire.eval360.projects.domain.request.CreateEvaluee;
import com.gire.eval360.projects.domain.request.CreateFeedbackProvider;
import com.gire.eval360.projects.domain.request.CreateProjectAdmin;
import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.domain.request.CreateReviewer;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;
import com.gire.eval360.projects.repository.EvalueeFeedbackProviderRepository;
import com.gire.eval360.projects.repository.ProjectRepository;
import com.gire.eval360.projects.service.remote.UserServiceRemote;
import com.gire.eval360.projects.service.remote.dto.users.UserResponse;

@Service
public class ProjectServiceImpl implements ProjectService{
	
	private final ProjectRepository projectRepository;
	
	private final EvalueeFeedbackProviderRepository efpRepository;
	
	private final NotificationFeedBackSender notificationFeedBackSender;
	
	private final UserServiceRemote userServiceRemote;
	
	@Autowired
	public ProjectServiceImpl(final ProjectRepository projectRepository,
							  final EvalueeFeedbackProviderRepository efpRepository,
							  final NotificationFeedBackSender notificationFeedBackSender,
							  final UserServiceRemote userServiceRemote) {
		this.projectRepository = projectRepository;
		this.efpRepository = efpRepository;
		this.notificationFeedBackSender = notificationFeedBackSender;
		this.userServiceRemote = userServiceRemote;
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
		EvalueeFeedbackProvider efp = efpRepository.findByEvalueeAndFeedbackProvider(request.getIdEvaluee(), request.getIdFeedbackProvider());
		efp.setStatus(EvaluationStatus.RESUELTO);
		sendNotificationReviewerForFeedbackComplete(efp);
	}
	
	/**
	 * Evalua si no quedaron feedbacks pendientes. En caso de que no queden feedbacks pendientes, significa que se ha completado la evaluación y por lo cual
	 * se enviará la notificación al evaluado (quien pasará a ser reviewer), para que pueda ingresar a la aplicación y ver el resultado de la evaluación.
	 * @param efp
	 */
	private void sendNotificationReviewerForFeedbackComplete(EvalueeFeedbackProvider efp) {
		List<EvalueeFeedbackProvider> providers = efp.getEvaluee().getFeedbackProviders();
		Predicate<EvalueeFeedbackProvider> predicate = e -> e.getStatus().equals(EvaluationStatus.PENDIENTE);
		Optional<EvalueeFeedbackProvider> fpPendiente=providers.stream().filter(predicate).findAny();
		if(!fpPendiente.isPresent()) {
			Evaluee evaluee = efp.getEvaluee();
			Project project = evaluee.getProject();
			sendNotificationEvaluationCompleteToReviewer(evaluee.getIdUser(),evaluee.getId(),project.getId(),project.getIdEvaluationTemplate());
		}
	}
	
	/******* METODOS PRIVADOS *******/
	
	/**
	 * Arma el dto para enviar la notificación a los provideers, haciendoles llegar un mail, que se genero un nuevo proyecto de evaluación.
	 * @param projectGenerated
	 */
	private void armarFeedBack(Project projectGenerated) {
		
	
		for (Evaluee evaluee : projectGenerated.getEvaluees()) {
			
			for (EvalueeFeedbackProvider evalueeFp : evaluee.getFeedbackProviders()) {
				Long idFp = evalueeFp.getFeedbackProvider().getIdUser();
				Long idEvalueeFP=evalueeFp.getId();
				Long idProject=evaluee.getProject().getId();
				sendFeedBackToFeedbackProviders(idFp,idEvalueeFP,idProject);
			}
		}
			
	}

	/**
	 * Envia notificación a reviewer
	 * 
	 * @param idUser
	 * @param id
	 */
	private void sendNotificationEvaluationCompleteToReviewer(Long idUser, Long idEvaluee, Long idProject, Long idTemplate) {
		NotificationReviewerDto notifyFpDto = new NotificationReviewerDto(idUser,idEvaluee,idProject,idTemplate);
		notificationFeedBackSender.sendNotificationReviewer(notifyFpDto);	
	}
	
	/**
	 * Envia notificación a feedbackProviders
	 * @param idEvaluee
	 * @param idEvalueeFp
	 * @param idProject
	 */
	private void sendFeedBackToFeedbackProviders(Long idFp,Long idEvalueeFP,Long idProject) {
		NotificationFeedbackProviderDto notifyFpDto = new NotificationFeedbackProviderDto(idFp,idEvalueeFP,idProject);
		notificationFeedBackSender.sendNotificationFeedBackProvider(notifyFpDto);
	}
	
	/**
	 * Crea los project admins del proyecto de evaluación.
	 * 
	 * @param project
	 * @param createAdmins
	 * @param projectAdmins
	 */
	private void createProjectAdmin(Project project, List<CreateProjectAdmin> createAdmins,	List<ProjectAdmin> projectAdmins) {
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
	 * Crea el evaluee y posteriormente llama al metodo de createFeedBackProvider para asociar los proveedores de feedback para el evaluado creado.
	 * 
	 * @param mapaFps
	 * @param project
	 * @param createEvaluees
	 */
	private void createEvaluee(Map<Long, FeedbackProvider> mapaFps, Map<Long, Reviewer> mapaReviewers, List<Evaluee> evaluees, Project project, List<CreateEvaluee> createEvaluees) {
						
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
	private void createFeedBackProvider(Map<Long, FeedbackProvider> mapaFps, Project project, CreateEvaluee createEvaluee, Evaluee evaluee) {
		
		for (CreateFeedbackProvider createFp: createEvaluee.getFeedbackProviders()) {
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
	private void createReviewer(Map<Long, Reviewer> mapaReviewers, Project project, CreateEvaluee createEvaluee, Evaluee evaluee) {
		
		for (CreateReviewer createReviewer: createEvaluee.getReviewers()) {
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
			
			for (FeedbackProvider fp: p.getFeedbackProviders()){
				mapaFps.put(fp.getIdUser(), fp);
			}
			
			for (Reviewer reviewer: p.getReviewers()) {
				mapaReviewers.put(reviewer.getIdUser(), reviewer);
			}
			
			Evaluee evaluee = new Evaluee();
			evaluee.setIdUser(createEvaluee.getIdUser());
			evaluee.setProject(p);
			
			for (CreateFeedbackProvider createFp: createEvaluee.getFeedbackProviders()) {
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
				sendFeedBackToFeedbackProviders(fp.getIdUser(),efp.getId(), evaluee.getProject().getId());				
			}
			
			for (CreateReviewer createReviewer: createEvaluee.getReviewers()) {
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
			Optional<FeedbackProvider> feedbackProvider = p.getFeedbackProviders().stream().filter(fp -> fp.getIdUser().equals(idFp)).findFirst();
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
		ProjectStatusBuilder builder = ProjectStatus.builder().id(p.getId()).name(p.getName()).description(p.getDescription());
		
		List<EvalueeStatus> evalueesStatus = p.getEvaluees().stream().map(e -> buildEvalueeStatus(e)).collect(Collectors.toList());
		List<FeedbackProviderStatus> fpStatus = p.getFeedbackProviders().stream().map(fp -> buildFeedbackProviderStatus(fp)).collect(Collectors.toList());
		List<ReviewerStatus> reviewerStatus = p.getReviewers().stream().map(r -> buildReviewerStatus(r)).collect(Collectors.toList());
		List<AdminStatus> adminStatus = p.getProjectAdmins().stream().map(a -> buildAdminStatus(a)).collect(Collectors.toList());
		
		ProjectStatus projectStatus = builder.evalueesStatus(evalueesStatus)
			   .feedbackProvidersStatus(fpStatus)
			   .reviewersStatus(reviewerStatus)
			   .adminsStatus(adminStatus)
			   .build();
		return projectStatus;
	}
	
	private EvalueeStatus buildEvalueeStatus(Evaluee e) {
		UserResponse user = this.userServiceRemote.getUserById(e.getIdUser());
		
		EvalueeStatus evalueeStatus = EvalueeStatus.builder().id(e.getId())
															 .idUser(e.getIdUser())
															 .username(user.getUsername())
															 .avatar("")
															 .status(Status.PENDIENTE)
															 .completedFeedbacks(0)
															 .pendingFeedbacks(0)
															 .build();
		
		List<FeedbackProviderDetail> fpDetails = e.getFeedbackProviders().stream().map(efp -> buildFeedbackProviderDetail(efp)).collect(Collectors.toList());
		Integer totalFeedbacks = fpDetails.size();
		Integer completedFeedbacks = 0;
		for (FeedbackProviderDetail fpDetail : fpDetails) {
			if (fpDetail.getStatus().equals(EvaluationStatus.RESUELTO)) {
				completedFeedbacks ++;
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
																		  .avatar("")
																		  .idUser(efp.getFeedbackProvider().getIdUser())
																		  .username(user.getUsername())
																		  .status(efp.getStatus())
																		  .build();
																		  
		return fpDetail;
	}

	private FeedbackProviderStatus buildFeedbackProviderStatus(FeedbackProvider fp) {
		UserResponse user = this.userServiceRemote.getUserById(fp.getIdUser());
		
		FeedbackProviderStatus fpStatus = FeedbackProviderStatus.builder().id(fp.getId())
															 .idUser(fp.getIdUser())
															 .username(user.getUsername())
															 .avatar("")
															 .status(Status.PENDIENTE)
															 .completedFeedbacks(0)
															 .pendingFeedbacks(0)
															 .build();
		
		List<EvalueeDetail> evalueeDetails = fp.getEvaluees().stream().map(efp -> buildEvalueeDetail(efp)).collect(Collectors.toList());
		Integer totalFeedbacks = evalueeDetails.size();
		Integer completedFeedbacks = 0;
		for (EvalueeDetail evalueeDetail : evalueeDetails) {
			if (evalueeDetail.getStatus().equals(EvaluationStatus.RESUELTO)) {
				completedFeedbacks ++;
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
		UserResponse user = this.userServiceRemote.getUserById(efp.getFeedbackProvider().getIdUser());

		
		EvalueeDetail evalueeDetail = EvalueeDetail.builder().id(efp.getFeedbackProvider().getId())
																		  .avatar("")
																		  .idUser(efp.getFeedbackProvider().getIdUser())
																		  .username(user.getUsername())
																		  .status(efp.getStatus())
																		  .build();
																		  
		return evalueeDetail;
	}

	private ReviewerStatus buildReviewerStatus(Reviewer r) {
		UserResponse user = this.userServiceRemote.getUserById(r.getIdUser());
		
		ReviewerStatus reviewerStatus = ReviewerStatus.builder().id(r.getId())
															 .idUser(r.getIdUser())
															 .username(user.getUsername())
															 .avatar("")
															 .status(Status.PENDIENTE)
															 .completedReports(0)
															 .pendingReports(0)
															 .build();
		
		
		return reviewerStatus;
	}

	private EvalueeDetail buildEvalueeReviewerDetail(EvalueeReviewer er) {
		// TODO Auto-generated method stub
		return null;
	}

	private AdminStatus buildAdminStatus(ProjectAdmin a) {
		UserResponse user = this.userServiceRemote.getUserById(a.getIdUser());
		
		AdminStatus adminStatus = AdminStatus.builder().id(a.getId())
															 .idUser(a.getIdUser())
															 .username(user.getUsername())
															 .avatar("")
															 .creator(a.getCreator())
															 .build();
				
		return adminStatus;
	}

}
