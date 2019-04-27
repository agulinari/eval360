package com.gire.eval360.projects.service.remote.dto.evaluations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluatedsRequest {
	
	private Long idProject;
	private Long idFeedbackProvider;

}
