package com.gire.eval360.projects.domain.request.sheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class FieldUser {
	
	String username;
	String rol;
	String relationship;
	String evalueeUsername;

}
