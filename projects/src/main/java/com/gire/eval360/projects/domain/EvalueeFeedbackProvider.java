package com.gire.eval360.projects.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvalueeFeedbackProvider {

	@EmbeddedId
	private EvalueeFeedbackProviderId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("evalueeId")
	private Evaluee evaluee;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("feedbackProviderId")
	private FeedbackProvider feedbackProvider;

	@Column(name = "created_on")
	private Date createdOn = new Date();

	public EvalueeFeedbackProvider(Evaluee evaluee, FeedbackProvider feedbackProvider, Long idEvaluation) {
		this.evaluee = evaluee;
		this.feedbackProvider = feedbackProvider;
		this.id = new EvalueeFeedbackProviderId(evaluee.getId(), feedbackProvider.getId(), idEvaluation);
	}

	
}
