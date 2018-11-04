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
public class Evaluated  extends AuditedEntity{

	@JsonManagedReference
    @OneToOne(mappedBy = "evaluated", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Employee employee;
	
	@JsonManagedReference
    @OneToOne(mappedBy = "evaluated", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Project project;

	@OneToMany(mappedBy = "evaluated", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<FeedbackProvider> feedbacksProviders;
}
