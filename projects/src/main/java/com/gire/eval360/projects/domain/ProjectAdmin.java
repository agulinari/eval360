package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectAdmin extends ProjectMember{

	private static final long serialVersionUID = 1L;
	
	@OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
    private List<Project> projectsCreated;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID")    
	private Project project;
}
