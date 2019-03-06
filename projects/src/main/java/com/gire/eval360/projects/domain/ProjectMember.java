package com.gire.eval360.projects.domain;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode(of= {}, callSuper = true)
public abstract class ProjectMember extends AuditedEntity{
			
	@Column
	@NonNull
	private Long idUser;
	
	@JsonIgnore
	@ManyToOne	
	private Project project;
	
}
