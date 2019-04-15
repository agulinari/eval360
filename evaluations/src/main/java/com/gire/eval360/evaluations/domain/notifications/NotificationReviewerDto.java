package com.gire.eval360.evaluations.domain.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class NotificationReviewerDto {

	private Long idFeedbackProvider; //0 -> all FPs evaluated this evaluee, !0 -> id of FP
	private Long idEvaluee;
	private Long idProject;
	private Long idTemplate;
	
}