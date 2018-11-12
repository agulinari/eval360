package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	@NonNull
	private Status status;
		
	private Long idEvaluationTemplate;

	private Long idReportTemplate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADMIN_CREATOR_ID")    
	private ProjectAdmin creator;
		
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "project_reviewer", 
	    joinColumns = { @JoinColumn(name = "ID") }, 
	    inverseJoinColumns = { @JoinColumn(name = "REVIEWER_ID") })
	private List<Reviewer> reviewers;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "project_teamMember", 
	    joinColumns = { @JoinColumn(name = "ID") }, 
	    inverseJoinColumns = { @JoinColumn(name = "TEAM_MEMBER_ID") })
	private List<ProjectAdmin> teamMembers;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "project_feedbackprovider", 
	    joinColumns = { @JoinColumn(name = "ID") }, 
	    inverseJoinColumns = { @JoinColumn(name = "FEEDBACKPROVIDER_ID") })
	private List<FeedbackProvider> feedbackProviders;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Evaluee> evaluees;

}
