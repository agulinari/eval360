package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Data
public class Project extends AuditedEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	@NonNull
	private Status status;
		
	private Long idEvaluationTemplate;

	private Long idReportTemplate;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="CREATOR_ID") 
	private ProjectAdmin creator;
		
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Reviewer> reviewers;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<ProjectAdmin> teamMembers;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<FeedbackProvider> feedbackProviders;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Evaluee> evaluees;

}
