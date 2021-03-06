package com.gire.eval360.notifications.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class NotificationFeedbackProviderDto {
	
	private Long idUserFeedback;
	private Long idEvalueeFP;
	private Long idProject;
	
}
