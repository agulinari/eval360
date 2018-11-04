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

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
public class FeedbackProvider extends AuditedEntity{

	@JsonManagedReference
    @OneToOne(mappedBy = "feedbackProvider", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Employee employee;
	
	@OneToMany(mappedBy = "feedbackProvider", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
    private List<Project> projects;
		
}
