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
public class TeamMember extends User{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManyToMany(mappedBy = "teamMembers")
    private List<Project> projects;

}
