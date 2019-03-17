package com.gire.eval360.projects.domain.excel;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectEvalueeExcel extends ProjectMemberExcel {

	private List<ProjectFpExcel> feedbackProviders;
	private List<ProjectReviewerExcel> reviewers;
}
