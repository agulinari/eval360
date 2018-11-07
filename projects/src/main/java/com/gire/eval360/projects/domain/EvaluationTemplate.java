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
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
public class EvaluationTemplate extends AuditedEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	@NotBlank
	String title;
	
	@Column
	@NotBlank
	Category category;
	
	@OneToMany(mappedBy = "template", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
	List<Section> sections;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID") 
	private Project project;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_CREATOR_EVALUATION_TEMPLATE_ID")
	private User creatorEvaluationTemplate;
}
