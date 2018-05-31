package com.gire.eval360.domain;

import java.util.Collection;

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
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class Area extends AuditedEntity{
	
	@Column
	private long code;
	
	@Column
	@NonNull
	private String name;
	
	@Column
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "area_id")
	private Area dependentArea;
	
	@OneToMany(mappedBy = "dependentArea", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JsonIgnore
	private Collection<Area> subAreas;
	
	@OneToMany(mappedBy = "area", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JsonIgnore
	private Collection<Employee> employees;

}
