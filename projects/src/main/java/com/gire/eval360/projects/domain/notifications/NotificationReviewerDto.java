package com.gire.eval360.projects.domain.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class NotificationReviewerDto {

	private Long idEvalueeUser;
	private Long idProject;
	
}
