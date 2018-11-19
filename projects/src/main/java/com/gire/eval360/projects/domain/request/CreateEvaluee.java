package com.gire.eval360.projects.domain.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CreateEvaluee {

	private Long id;
	private Long idUser;
	private List<CreateFeedbackProvider> feedbackProviders;
	
}
