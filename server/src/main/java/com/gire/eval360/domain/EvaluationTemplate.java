package com.gire.eval360.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
public class EvaluationTemplate extends AuditedEntity{

	@Column
	@NotBlank
	String title;
	
	@Column
	@NotBlank
	Category category;
	
	@OneToMany(mappedBy = "template", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
	List<Section> sections;
	
	@ManyToOne
	@JoinColumn(name = "creator_id")
	@JsonIgnore
	Employee creator;
}
