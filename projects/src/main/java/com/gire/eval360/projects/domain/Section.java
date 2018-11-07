package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
public class Section extends AuditedEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	@NonNull
	private String name;
	
	@Column
	@NonNull
	private String description;
	
    @ManyToOne
	@JoinColumn(name = "EVALUATION_TEMPLATE_ID")
    @JsonIgnore
	private EvaluationTemplate template;

	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<ItemTemplate> itemTemplate;

}
