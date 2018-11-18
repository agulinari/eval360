package com.gire.eval360.projects.domain.projection;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.Reviewer;

@Projection(name = "reviewerUsers", types = { Project.class }) 
public interface ReviewerUser {

	List<Reviewer> getReviewers();
	
}
