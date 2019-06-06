package com.gire.eval360.automation.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper=false)
public class FeedbackProvider extends User{
	
	private Relationship relationship;
	
	@Builder
    public FeedbackProvider(Long id, Long idUser, Relationship relationship) {
        super(id, idUser);
        this.relationship = relationship;
    }
}
