package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Evaluee extends User{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID")    
    private Project project;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "evaluee_feedbackprovider", 
	    joinColumns = { @JoinColumn(name = "ID") }, 
	    inverseJoinColumns = { @JoinColumn(name = "FEEDBACKPROVIDER_ID") })
	private List<FeedbackProvider> feedbacksProviders;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EVALUATION_ID") 
    private Evaluation evaluation;
}
