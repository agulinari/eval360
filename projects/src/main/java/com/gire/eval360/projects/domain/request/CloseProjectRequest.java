package com.gire.eval360.projects.domain.request;

import java.util.List;

import com.gire.eval360.projects.domain.request.CreateEvaluee.CreateEvalueeBuilder;

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
public class CloseProjectRequest {
	
	private Long idProject;

}
