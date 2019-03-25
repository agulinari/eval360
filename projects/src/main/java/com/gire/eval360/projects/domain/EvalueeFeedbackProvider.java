package com.gire.eval360.projects.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(of= {"id"})
public class EvalueeFeedbackProvider {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinColumn(name="evaluee_id", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JsonIgnore
	private Evaluee evaluee;

	@JoinColumn(name="feedback_provider_id", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private FeedbackProvider feedbackProvider;
	    
	@Enumerated(EnumType.STRING)
	@NonNull
    @Column
    private EvaluationStatus status;
    
	@Enumerated(EnumType.STRING)
	@NonNull
	@Column
	private Relationship relationship;

	@Column(name = "created_on")
	private Date createdOn = new Date();
	

	public EvalueeFeedbackProvider(Evaluee evaluee, FeedbackProvider feedbackProvider, Long idEvaluation) {
		this.evaluee = evaluee;
		this.feedbackProvider = feedbackProvider;
	}

	
}
