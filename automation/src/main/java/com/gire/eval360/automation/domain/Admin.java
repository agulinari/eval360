package com.gire.eval360.automation.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@EqualsAndHashCode(callSuper=false)
public class Admin extends User{

	private Boolean creator;
	
	@Builder
    public Admin(Long id, Long idUser, Boolean creator) {
        super(id, idUser);
        this.creator = creator;
    }

}
