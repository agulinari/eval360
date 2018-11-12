package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectMember extends AuditedEntity{

	private static final long serialVersionUID = 1L;
	
	private Long idUser;
	
	@OneToMany(mappedBy = "teamMembers", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Project> projects;
	
}
