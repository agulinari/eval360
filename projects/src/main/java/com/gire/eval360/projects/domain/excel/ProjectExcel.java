package com.gire.eval360.projects.domain.excel;

import java.util.List;

import lombok.Data;

@Data
public class ProjectExcel {

	private String name;
	private String description;
	private String template;
	private List<ProjectEvalueeExcel> evaluees;
	private List<ProjectAdminExcel> admins;
	
}
