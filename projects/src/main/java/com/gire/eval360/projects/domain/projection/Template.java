package com.gire.eval360.projects.domain.projection;

import org.springframework.data.rest.core.config.Projection;

import com.gire.eval360.projects.domain.Project;

@Projection(name = "template", types = { Project.class }) 
public interface Template {

	Long getIdEvaluationTemplate();
	
}
