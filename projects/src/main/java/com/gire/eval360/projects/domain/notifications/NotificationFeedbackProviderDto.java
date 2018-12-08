package com.gire.eval360.projects.domain.notifications;

import com.gire.eval360.projects.domain.EvaluationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class NotificationFeedbackProviderDto {
	
	private Long idUser;
	private String relationship;
    private EvaluationStatus status;
    			
}
