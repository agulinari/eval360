package com.gire.eval360.projects.domain.dto;

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
public class PendingEvaluee {

	private Long id;
	private Long idEvalueeFp;
	private Long idUser;
	private String username;
	private String avatar;
	private String relationShip;
}
