package com.gire.eval360.projects.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.gire.eval360.projects.domain.Evaluee;
import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.Reviewer;
import com.gire.eval360.projects.domain.dto.CompletedEvaluee;
import com.gire.eval360.projects.domain.dto.PendingEvaluee;
import com.gire.eval360.projects.domain.dto.ProjectStatus;
import com.gire.eval360.projects.domain.excel.ProjectExcel;
import com.gire.eval360.projects.domain.history.UserHistory;
import com.gire.eval360.projects.domain.request.CloseProjectRequest;
import com.gire.eval360.projects.domain.request.CreateEvaluee;
import com.gire.eval360.projects.domain.request.CreateProjectAdmin;
import com.gire.eval360.projects.domain.request.CreateProjectRequest;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;
import com.gire.eval360.projects.domain.stats.ActiveProjectStats;

public interface ProjectService {
	
	Collection<Project> getProjects();

	Project createProject(CreateProjectRequest request);

	void reportFeedback(ReportFeedbackRequest request);

	void addEvaluee(Long id, CreateEvaluee request);

	void addAdmin(Long id, CreateProjectAdmin request);
	
	List<Evaluee> getEvalueesForFeedback(Long id, Long idFp);

	Optional<ProjectStatus> getProjectStatus(Long id);

	List<PendingEvaluee> getPendingEvalueesForUser(Long id, Long idFp);

	List<CompletedEvaluee> getCompletedEvalueesForUser(Long id, Long idReviewer);
	
	List<Reviewer> getReviewers(Long id, Long idEvaluee);

	Project importProject(ProjectExcel projectExcel);
	
	UserHistory getUserHistory(Long idUser);

	List<ActiveProjectStats> getActiveProjectsStats();

	boolean closeProject(CloseProjectRequest request);


}
