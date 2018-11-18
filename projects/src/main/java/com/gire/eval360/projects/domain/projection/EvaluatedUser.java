package com.gire.eval360.projects.domain.projection;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.gire.eval360.projects.domain.Evaluee;
import com.gire.eval360.projects.domain.Project;


@Projection(name = "evaluatedUsers", types = { Project.class }) 
public interface EvaluatedUser {
	
	List<Evaluee> getEvaluees();
	
}
