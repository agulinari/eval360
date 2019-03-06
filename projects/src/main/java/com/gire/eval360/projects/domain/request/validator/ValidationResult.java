package com.gire.eval360.projects.domain.request.validator;

import java.util.Optional;

public interface ValidationResult {

	static ValidationResult valid(){
		return ValidationSupport.valid();
	}

	static ValidationResult invalid(String reason){
		return new Invalid(reason);
	}

	boolean isValid();

	Optional<String> getReason();


}
