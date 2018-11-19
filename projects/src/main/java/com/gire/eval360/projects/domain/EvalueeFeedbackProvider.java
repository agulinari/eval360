package com.gire.eval360.projects.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class EvalueeFeedbackProvider {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private Evaluee evaluee;

	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private FeedbackProvider feedbackProvider;
	
    @Column
    private Long evaluationId;
    
	@Column
	private String relationship;

	@Column(name = "created_on")
	private Date createdOn = new Date();
	

	public EvalueeFeedbackProvider(Evaluee evaluee, FeedbackProvider feedbackProvider, Long idEvaluation) {
		this.evaluee = evaluee;
		this.feedbackProvider = feedbackProvider;
	}

	
}
