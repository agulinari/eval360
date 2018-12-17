package com.gire.eval360.projects.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.gire.eval360.projects.domain.notifications.NotificationFeedbackProviderDto;
import com.gire.eval360.projects.domain.request.CreateEvaluee;
import com.gire.eval360.projects.domain.request.CreateFeedbackProvider;
import com.gire.eval360.projects.domain.request.CreateProjectAdmin;
import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.domain.request.CreateReviewer;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;
import com.gire.eval360.projects.repository.EvalueeFeedbackProviderRepository;
import com.gire.eval360.projects.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService{
	
	private final ProjectRepository projectRepository;
	
	private final EvalueeFeedbackProviderRepository efpRepository;
	
	private final NotificationFeedBackSender notificationFeedBackSender;
	
	@Autowired
	public ProjectServiceImpl(final ProjectRepository projectRepository,final EvalueeFeedbackProviderRepository efpRepository,
							  final NotificationFeedBackSender notificationFeedBackSender) {
		this.projectRepository = projectRepository;
		this.efpRepository = efpRepository;
		this.notificationFeedBackSender = notificationFeedBackSender;
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
		Project projectGenerated=projectRepository.save(project);
		notifyFeedBack(projectGenerated);
		
		
		return project;
	}
	
	@Override
	@Transactional
	public void reportFeedback(ReportFeedbackRequest request) {
		EvalueeFeedbackProvider efp = efpRepository.findByEvalueeAndFeedbackProvider(request.getIdEvaluee(), request.getIdFeedbackProvider());
		efp.setStatus(EvaluationStatus.RESUELTO);
	}
	
	/******* METODOS PRIVADOS *******/
	
	/**
	 * Notifica a los provideers, haciendoles llegar un mail, que se genero un nuevo proyecto de evaluación.
	 * @param projectGenerated
	 */
	private void notifyFeedBack(Project projectGenerated) {
		
	
		for (Evaluee evaluee : projectGenerated.getEvaluees()) {
			
			for (EvalueeFeedbackProvider evalueeFp : evaluee.getFeedbackProviders()) {
				NotificationFeedbackProviderDto notifyFpDto = new NotificationFeedbackProviderDto(evalueeFp.getId(),evalueeFp.getRelationship(),
																								 evalueeFp.getStatus());
				notificationFeedBackSender.sendNotification(notifyFpDto);
			}
		}
			
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
}
