package com.gire.eval360.projects.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gire.eval360.projects.repository.ProjectRepository;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	@SuppressWarnings("unused")
	private final ProjectRepository projectRepository;
	
	@Autowired
	public ProjectController(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}
}
