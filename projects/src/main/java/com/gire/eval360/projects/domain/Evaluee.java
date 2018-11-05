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
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="project_id")    
    private Project project;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	    name = "evaluee_feedbackprovider", 
	    joinColumns = { @JoinColumn(name = "id") }, 
	    inverseJoinColumns = { @JoinColumn(name = "feedbackProviderID") })
	private List<FeedbackProvider> feedbacksProviders;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="evaluation_id") 
    private Evaluation evaluation;
}
