package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
public class FeedbackProvider extends User{

		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToMany(mappedBy = "feedbacksProviders", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
    private List<Project> projects;
	
	@ManyToMany(mappedBy = "feedbacksProviders")
    private List<Evaluation> evaluations;
	
	@ManyToMany(mappedBy = "feedbacksProviders")    
	private List<Evaluee> evaluees; 
		
}
