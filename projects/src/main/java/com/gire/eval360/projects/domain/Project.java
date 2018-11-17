package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Data
public class Project extends AuditedEntity{

	
	@Column(unique=true)
	@NonNull
	private String name;
	
	@Column
	private String description;

	@Column
	@NonNull
	@Enumerated(EnumType.STRING)
	private Status status;
		
	private Long idEvaluationTemplate;

	private Long idReportTemplate;
			
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Reviewer> reviewers;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<ProjectAdmin> teamMembers;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<FeedbackProvider> feedbackProviders;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Evaluee> evaluees;

}
