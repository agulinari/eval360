package com.gire.eval360.projects.domain.request;

import com.gire.eval360.projects.domain.Relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class CreateFeedbackProvider {

	private Long id;
	private Long idUser;
	private Relationship relationship;
	
}
