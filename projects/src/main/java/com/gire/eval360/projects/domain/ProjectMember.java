package com.gire.eval360.projects.domain;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class ProjectMember extends AuditedEntity{

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID")    
	private Project project;
	
	@Column
	@NonNull
	private Long idUser;
	
}
