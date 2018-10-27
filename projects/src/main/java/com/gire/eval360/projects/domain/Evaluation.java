package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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

	@ManyToOne
	@JoinColumn(name = "evaluator_id")
	private Employee evaluator;
	
	@ManyToOne
	@JoinColumn(name = "evaluated_id")
	private Employee evaluated;
	
	@ManyToOne
	@JoinColumn(name = "template_id")
	private EvaluationTemplate evaluationTemplate;
	
	@Column
	@NonNull
	private Status status;
	
	@OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Item> items;
	
	@Column
	@NotBlank
	private String commentary;
	
}
