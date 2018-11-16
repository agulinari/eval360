package com.gire.eval360.templates.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EvaluationTemplate extends AuditedEntity{

	@Column(unique = true)
	@NonNull
	private String title;
	
	@Column
	@NonNull
	private  String idUser;

		
	//@OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "template_id", nullable = false, updatable = false)
	private Collection<Section> sections;
	
	
}
