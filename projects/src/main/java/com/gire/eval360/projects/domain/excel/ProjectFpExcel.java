package com.gire.eval360.projects.domain.excel;

import com.gire.eval360.projects.domain.Relationship;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectFpExcel extends ProjectMemberExcel {
	
	private Relationship relationship;
	
}
