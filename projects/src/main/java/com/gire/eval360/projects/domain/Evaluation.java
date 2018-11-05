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
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
public class Evaluation extends AuditedEntity{
	
	@Column
	@NonNull
	private Status status;
	
	@Column
	@NotBlank
	private String commentary;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="project_id") 
	private Project project;

	@ManyToOne
	@JoinColumn(name = "template_id")
	private EvaluationTemplate evaluationTemplate;
	
	@OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Item> items;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "evaluation_feedbackprovider", 
	    joinColumns = { @JoinColumn(name = "id") }, 
	    inverseJoinColumns = { @JoinColumn(name = "feedbackProviderID") })
	private List<FeedbackProvider> feedbacksProviders;
	
	@OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Evaluee> evaluees;
	
}
