package com.gire.eval360.templates.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Section extends AuditedEntity{
	
	@Column
	@NonNull
	private String name;
	
	@Column
	@NonNull
	private String description;
	
	@Column
	@NonNull
	@Enumerated(EnumType.STRING)
	private SectionType sectionType;
	
	@Column
	@NonNull
	private Integer position;
	
    @ManyToOne
	@JoinColumn(name = "template_id", insertable=false, updatable=false)
    @JsonIgnore
	private EvaluationTemplate evaluation;

	//@OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "section_id", nullable = false, updatable = false)
	private Collection<ItemTemplate> items;
	
}
