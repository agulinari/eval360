package com.gire.eval360.automation.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper=false)
public class Reviewer extends User{

	@Builder
    public Reviewer(Long id, Long idUser) {
        super(id, idUser);
    }
	
}
