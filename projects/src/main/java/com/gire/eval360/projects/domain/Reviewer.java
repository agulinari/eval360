package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Reviewer  extends AuditedEntity{

	@JsonManagedReference
    @OneToOne(mappedBy = "reviewer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Employee employee;
	
	@OneToMany(mappedBy = "reviewer", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
    private List<Project> projects;

}
