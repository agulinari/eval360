package com.gire.eval360.projects.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CreateProjectAdmin {
	
	private Long id;
	private Long idUser;
	private Boolean creator;

}
