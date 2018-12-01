package com.gire.eval360.projects.service;

import java.util.Collection;

import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;

public interface ProjectService {
	
	Collection<Project> getProjects();

	Project createProject(CreateProjectRequest request);

	void reportFeedback(ReportFeedbackRequest request);

}
