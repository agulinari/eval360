package com.gire.eval360.projects.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;
import com.gire.eval360.projects.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	private final ProjectService projectService;
	
	@Autowired
	public ProjectController(final ProjectService projectService) {
		this.projectService= projectService;
	}
	
	
	@PostMapping("/create")
	public ResponseEntity<?> createProject(@RequestBody CreateProjectRequest request) {
		// TODO: validate request
		this.projectService.createProject(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/reportFeedback")
	public ResponseEntity<?> reportFeedback(@RequestBody ReportFeedbackRequest request) {
		this.projectService.reportFeedback(request);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
