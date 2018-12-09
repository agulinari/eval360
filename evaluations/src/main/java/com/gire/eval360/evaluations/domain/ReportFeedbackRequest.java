package com.gire.eval360.evaluations.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ReportFeedbackRequest {
	
	private Long idEvaluee;
	private Long idFeedbackProvider;

}
