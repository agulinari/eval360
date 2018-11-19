package com.gire.eval360.projects.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gire.eval360.projects.domain.Evaluee;
import com.gire.eval360.projects.domain.EvalueeFeedbackProvider;
import com.gire.eval360.projects.domain.FeedbackProvider;
import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.request.CreateEvaluee;
import com.gire.eval360.projects.domain.request.CreateFeedbackProvider;
import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService{
	
	private ProjectRepository projectRepository;
	
	public ProjectServiceImpl(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
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
		
		List<CreateEvaluee> createEvaluees = request.getEvaluees();
		List<Evaluee> evaluees = new ArrayList<>();
		
		for (CreateEvaluee createEvaluee : createEvaluees) {
			Evaluee evaluee = new Evaluee();
			evaluee.setIdUser(createEvaluee.getIdUser());
			evaluee.setProject(project);
			
			for (CreateFeedbackProvider createFp: createEvaluee.getFeedbackProviders()) {
				EvalueeFeedbackProvider efp = new EvalueeFeedbackProvider();
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
		
		projectRepository.save(project);
		return project;
	}
}
