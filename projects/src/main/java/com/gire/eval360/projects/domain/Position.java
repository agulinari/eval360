package com.gire.eval360.projects.domain;

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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class Position extends AuditedEntity{

	@Column
	private long code;
	
	@Column
	@NonNull
	private String name;
	
	@Column
	private String description;
	
	@OneToMany(mappedBy = "position", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JsonIgnore
	private Collection<Employee> employees;

}
