package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends AuditedEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	@NonNull
	private String firstName;
	
	@Column
	@NonNull
	private String lastName;
	
	@Column
	@NonNull
	private String email;

	@OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Project> createdProjects;
	
//	@OneToMany(mappedBy = "creatorReportTemplate", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
//	private List<ReportTemplate> reportsTemplates;
	
	@OneToOne(mappedBy = "creatorEvaluationTemplate", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private EvaluationTemplate evaluationTemplate;
}
