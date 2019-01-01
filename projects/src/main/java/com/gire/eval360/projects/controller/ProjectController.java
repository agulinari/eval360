package com.gire.eval360.projects.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gire.eval360.projects.domain.Evaluee;
import com.gire.eval360.projects.domain.dto.ProjectStatus;
import com.gire.eval360.projects.domain.request.CreateEvaluee;
import com.gire.eval360.projects.domain.request.CreateProjectAdmin;
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
	
	@PostMapping("/{id}/addEvaluee")
	public ResponseEntity<?> addEvaluee(@PathVariable Long id, @RequestBody CreateEvaluee request) {
		// TODO: validate request
		this.projectService.addEvaluee(id, request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/{id}/addAdmin")
	public ResponseEntity<?> addAdmin(@PathVariable Long id, @RequestBody CreateProjectAdmin request) {
		// TODO: validate request
		this.projectService.addAdmin(id, request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}/feedbackProvider/{idFp}/evaluees")
	public List<Evaluee> getEvalueesForFeedback(@PathVariable Long id, @PathVariable Long idFp ) {
		return this.projectService.getEvalueesForFeedback(id, idFp);
	}
	
	@GetMapping("/{id}/status")
	public ResponseEntity<ProjectStatus> getProjectStatus(@PathVariable Long id) {
		Optional<ProjectStatus> projectStatus = this.projectService.getProjectStatus(id);
		return projectStatus.map(p -> ResponseEntity.ok().body(p)).orElse(ResponseEntity.notFound().build());
	}

}
