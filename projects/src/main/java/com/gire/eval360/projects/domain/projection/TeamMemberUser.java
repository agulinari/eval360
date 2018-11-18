package com.gire.eval360.projects.domain.projection;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.ProjectAdmin;

@Projection(name = "teamMemberUsers", types = { Project.class }) 
public interface TeamMemberUser {

	List<ProjectAdmin> getTeamMembers();
	
}
