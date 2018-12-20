package com.gire.eval360.notifications.domain;

import lombok.Data;

@Data
public class EvaluationDto {

	private String id;
	private Long idProject;
	private Long idFeedbackProvider;
	private Long idEvaluee;
	
}
