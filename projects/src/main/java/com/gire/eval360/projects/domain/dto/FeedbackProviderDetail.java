package com.gire.eval360.projects.domain.dto;

import com.gire.eval360.projects.domain.EvaluationStatus;

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
public class FeedbackProviderDetail {

	private Long id;
	private Long idUser;
	private String username;
	private String avatar;
	private EvaluationStatus status;
	
}
