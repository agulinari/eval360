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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	@NonNull
	private Status status;
	
	@Column
	@NotBlank
	private String commentary;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID") 
	private Project project;

	@ManyToOne
	@JoinColumn(name = "EVALUATION_TEMPLATE_ID")
	private EvaluationTemplate evaluationTemplate;
	
	@OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Item> items;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "evaluation_feedbackprovider", 
	    joinColumns = { @JoinColumn(name = "ID") }, 
	    inverseJoinColumns = { @JoinColumn(name = "FEEDBACKPROVIDER_ID") })
	private List<FeedbackProvider> feedbacksProviders;
	
	@OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Evaluee> evaluees;
	
}
