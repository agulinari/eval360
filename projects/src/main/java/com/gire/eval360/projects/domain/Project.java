package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
public class Project extends AuditedEntity{
	
	@Column
	@NonNull
	private Status status;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Evaluation> evaluations;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<EvaluationTemplate> evaluationTemplates;
	
	@OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private ReportTemplate report;
	
	@OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private Employee creator;
		
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Reviewer> reviewers;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<TeamMember> teamMembers;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<FeedbackProvider> feedbacksProviders;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Evaluated> evaluated;

}
