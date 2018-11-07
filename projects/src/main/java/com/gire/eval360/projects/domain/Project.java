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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Builder
@AllArgsConstructor
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
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Evaluation> evaluations;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<EvaluationTemplate> evaluationTemplates;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ReportTemplate> reports;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USER_CREATOR_PROJECT_ID")    
	private User creator;
		
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
	private List<TeamMember> teamMembers;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "project_feedbackprovider", 
	    joinColumns = { @JoinColumn(name = "ID") }, 
	    inverseJoinColumns = { @JoinColumn(name = "FEEDBACKPROVIDER_ID") })
	private List<FeedbackProvider> feedbacksProviders;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Evaluee> evaluees;

}
