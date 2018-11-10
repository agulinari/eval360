package com.gire.eval360.templates.domain;

import java.sql.Date;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class EvaluationTemplate extends AuditedEntity{

	@Column
	@NonNull
	private String title;
	
	@Column
	@NonNull
	private  String idUser;

		
	@OneToMany(mappedBy = "evaluation", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JsonIgnore
	private Collection<Section> sections;
	
	
}
