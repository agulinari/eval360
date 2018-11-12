package com.gire.eval360.projects.domain;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class EvalueeFeedbackProvider extends AuditedEntity{

	private static final long serialVersionUID = 1L;

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
		this.id = new EvalueeFeedbackProviderId(evaluee.getId(), feedbackProvider.getId(), feedbackProvider.getIdEvaluation());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass())
			return false;

		EvalueeFeedbackProvider that = (EvalueeFeedbackProvider) o;
		return Objects.equals(evaluee, that.evaluee) &&
				Objects.equals(feedbackProvider, that.feedbackProvider);
	}

	@Override
	public int hashCode() {
		return Objects.hash(evaluee, feedbackProvider);
	}

}
