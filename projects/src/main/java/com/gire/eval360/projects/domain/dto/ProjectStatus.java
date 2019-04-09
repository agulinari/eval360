package com.gire.eval360.projects.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class ProjectStatus {

	private Long id;
	private Long idTemplate;
	private String name;
	private String description;
	private List<EvalueeStatus> evalueesStatus;
	private List<FeedbackProviderStatus> feedbackProvidersStatus;
	private List<ReviewerStatus> reviewersStatus;
	private List<AdminStatus> adminsStatus;
	
}
