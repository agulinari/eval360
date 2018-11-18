package com.gire.eval360.projects.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.repository.ProjectRepository;
import com.google.common.collect.Lists;

@Service
public class ProjectServiceImpl implements ProjectService{
	
	private ProjectRepository projectRepository;
	
	public ProjectServiceImpl(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public Collection<Project> getProjects() {
		return projectRepository.findAll();
	}
}
