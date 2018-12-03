package com.gire.eval360.projects.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gire.eval360.projects.domain.EvaluationStatus;
import com.gire.eval360.projects.domain.Evaluee;
import com.gire.eval360.projects.domain.EvalueeFeedbackProvider;
import com.gire.eval360.projects.domain.FeedbackProvider;
import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.ProjectAdmin;
import com.gire.eval360.projects.domain.Status;
import com.gire.eval360.projects.domain.request.CreateEvaluee;
import com.gire.eval360.projects.domain.request.CreateFeedbackProvider;
import com.gire.eval360.projects.domain.request.CreateProjectAdmin;
import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;
import com.gire.eval360.projects.repository.EvalueeFeedbackProviderRepository;
import com.gire.eval360.projects.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService{
	
	private final ProjectRepository projectRepository;
	
	private final EvalueeFeedbackProviderRepository efpRepository;
	
	@Autowired
	public ProjectServiceImpl(final ProjectRepository projectRepository,
			final EvalueeFeedbackProviderRepository efpRepository) {
		this.projectRepository = projectRepository;
		this.efpRepository = efpRepository;
	}

	public Collection<Project> getProjects() {
		return projectRepository.findAll();
	}

	@Override
	public Project createProject(CreateProjectRequest request) {

		Map<Long, FeedbackProvider> mapaFps = new HashMap<>();
		
		Project project = new Project();
		project.setName(request.getName());
		project.setDescription(request.getDescription());
		project.setIdEvaluationTemplate(request.getIdTemplate());
		project.setStatus(Status.PENDIENTE);
		
		List<CreateEvaluee> createEvaluees = request.getEvaluees();
		List<Evaluee> evaluees = new ArrayList<>();
		
		for (CreateEvaluee createEvaluee : createEvaluees) {
			Evaluee evaluee = new Evaluee();
			evaluee.setIdUser(createEvaluee.getIdUser());
			evaluee.setProject(project);
			
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
	
		project.setEvaluees(evaluees);
		project.setFeedbackProviders(mapaFps.values());
		
		List<CreateProjectAdmin> createAdmins = request.getAdmins();
		List<ProjectAdmin> projectAdmins = new ArrayList<>();
		
		for (CreateProjectAdmin createAdmin : createAdmins) {
			ProjectAdmin admin = new ProjectAdmin();
			admin.setIdUser(createAdmin.getIdUser());
			admin.setCreator(createAdmin.getCreator());
			admin.setCreatedDate(LocalDateTime.now());
			admin.setProject(project);
			projectAdmins.add(admin);
		}
		
		project.setProjectAdmins(projectAdmins);
		projectRepository.save(project);
		return project;
	}

	@Override
	@Transactional
	public void reportFeedback(ReportFeedbackRequest request) {
		EvalueeFeedbackProvider efp = efpRepository.findByEvalueeAndFeedbackProvider(request.getIdEvaluee(), request.getIdFeedbackProvider());
		efp.setStatus(EvaluationStatus.RESUELTO);
	}

	@Override
	@Transactional
	public void addEvaluee(Long id, CreateEvaluee createEvaluee) {
		
		Optional<Project> project = projectRepository.findById(id);
		
		project.ifPresent(p -> {
			
			Map<Long, FeedbackProvider> mapaFps = new HashMap<>();
			for (FeedbackProvider fp: p.getFeedbackProviders()){
				mapaFps.put(fp.getIdUser(), fp);
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
			p.getEvaluees().add(evaluee);
		});
		
	}
}
