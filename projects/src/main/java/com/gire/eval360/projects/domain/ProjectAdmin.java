package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectAdmin extends ProjectMember{

	private static final long serialVersionUID = 1L;

	@ManyToMany(mappedBy = "creator")
    private List<Project> projects;

}
