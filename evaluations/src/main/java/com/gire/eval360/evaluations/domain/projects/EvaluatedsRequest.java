package com.gire.eval360.evaluations.domain.projects;

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
