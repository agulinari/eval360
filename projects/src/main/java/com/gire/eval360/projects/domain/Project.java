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
import javax.persistence.OneToOne;

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
	
	@Column
	@NonNull
	private Status status;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Evaluation> evaluations;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<EvaluationTemplate> evaluationTemplates;
	
	@OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private ReportTemplate report;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")    
	private User creator;
		
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "project_reviewer", 
	    joinColumns = { @JoinColumn(name = "id") }, 
	    inverseJoinColumns = { @JoinColumn(name = "reviewerID") })
	private List<Reviewer> reviewers;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "project_teamMember", 
	    joinColumns = { @JoinColumn(name = "id") }, 
	    inverseJoinColumns = { @JoinColumn(name = "teamMemberID") })
	private List<TeamMember> teamMembers;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "project_feedbackprovider", 
	    joinColumns = { @JoinColumn(name = "id") }, 
	    inverseJoinColumns = { @JoinColumn(name = "feedbackProviderID") })
	private List<FeedbackProvider> feedbacksProviders;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Evaluee> evaluees;

}
