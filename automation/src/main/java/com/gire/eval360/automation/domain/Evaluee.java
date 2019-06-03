package com.gire.eval360.automation.domain;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

@Getter
@EqualsAndHashCode(callSuper=false)
public class Evaluee extends User{
	
	private @Singular List<FeedbackProvider> feedbackProviders;
	private @Singular List<Reviewer> reviewers;
	
	@Builder
    public Evaluee(Long id, Long idUser, List<FeedbackProvider> feedbackProviders, List<Reviewer> reviewers) {
        super(id, idUser);
        this.feedbackProviders = feedbackProviders;
        this.reviewers = reviewers;
    }
	
	@Builder
    public Evaluee(Long id, Long idUser, FeedbackProvider feedbackProvider, Reviewer reviewer) {
        super(id, idUser);
        this.feedbackProviders = Arrays.asList(feedbackProvider);
        this.reviewers = Arrays.asList(reviewer);
    }

}
