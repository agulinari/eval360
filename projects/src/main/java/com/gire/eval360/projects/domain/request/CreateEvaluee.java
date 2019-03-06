package com.gire.eval360.projects.domain.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class CreateEvaluee {

	private Long id;
	private Long idUser;
	private @Singular List<CreateFeedbackProvider> feedbackProviders;
	private @Singular List<CreateReviewer> reviewers;
	
}
