package com.gire.eval360.projects.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;

public class EvalueeFeedbackProviderId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "evaluee_id")
    private Long evalueeId;
 
    @Column(name = "feedbackProvider_id")
    private Long feedbackProviderId;
    
    @Column(name = "evaluation_id")
    private Long evaluationId;
 
    public EvalueeFeedbackProviderId(Long evalueeId, Long feedbackProviderId, Long evaluationId) {
        this.evalueeId = evalueeId;
        this.feedbackProviderId = feedbackProviderId;
        this.evaluationId = evaluationId;
    }
 
    //Getters omitted for brevity
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass()) 
            return false;
 
        EvalueeFeedbackProviderId that = (EvalueeFeedbackProviderId) o;
        return Objects.equals(evalueeId, that.evalueeId) && 
               Objects.equals(feedbackProviderId, that.feedbackProviderId) &&
               Objects.equals(evaluationId, that.evaluationId);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(evalueeId, feedbackProviderId, evaluationId);
    }

}
