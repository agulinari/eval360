package com.gire.eval360.projects.domain;


import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
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
			
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name = "project_id")
	private Collection<Reviewer> reviewers;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinColumn(name = "project_id")
	private Collection<ProjectAdmin> teamMembers;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "project_id")
	private Collection<FeedbackProvider> feedbackProviders;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "project_id")
	private Collection<Evaluee> evaluees;

}
