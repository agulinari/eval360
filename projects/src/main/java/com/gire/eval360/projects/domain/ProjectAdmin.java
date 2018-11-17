package com.gire.eval360.projects.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectAdmin extends ProjectMember{
	
	@Column
	@NonNull
	private Boolean creator;

}
