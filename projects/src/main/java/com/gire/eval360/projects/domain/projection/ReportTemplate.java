package com.gire.eval360.projects.domain.projection;

import org.springframework.data.rest.core.config.Projection;

import com.gire.eval360.projects.domain.Project;

@Projection(name = "reportTemplate", types = { Project.class }) 
public interface ReportTemplate {

	Long getIdReportTemplate();
	
}
