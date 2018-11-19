package com.gire.eval360.projects.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EvalueeFeedbackProviderId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "evaluee_id")
    private Long evalueeId;
 
    @Column(name = "feedbackProvider_id")
    private Long feedbackProviderId;
    
 
    public EvalueeFeedbackProviderId(Long evalueeId, Long feedbackProviderId, Long evaluationId) {
        this.evalueeId = evalueeId;
        this.feedbackProviderId = feedbackProviderId;
    }
 
    //Getters omitted for brevity
 
    public EvalueeFeedbackProviderId() {
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass()) 
            return false;
 
        EvalueeFeedbackProviderId that = (EvalueeFeedbackProviderId) o;
        return Objects.equals(evalueeId, that.evalueeId) && 
               Objects.equals(feedbackProviderId, that.feedbackProviderId);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(evalueeId, feedbackProviderId);
    }

	public Long getEvalueeId() {
		return evalueeId;
	}

	public void setEvalueeId(Long evalueeId) {
		this.evalueeId = evalueeId;
	}

	public Long getFeedbackProviderId() {
		return feedbackProviderId;
	}

	public void setFeedbackProviderId(Long feedbackProviderId) {
		this.feedbackProviderId = feedbackProviderId;
	}

    
}
