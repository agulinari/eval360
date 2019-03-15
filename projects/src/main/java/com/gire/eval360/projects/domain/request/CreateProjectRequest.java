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
public class CreateProjectRequest {
	
	private Long id;
	private Long idTemplate;
	private String name;
	private String description;
	private @Singular List<CreateEvaluee> evaluees;
	private @Singular List<CreateProjectAdmin> admins;

}
