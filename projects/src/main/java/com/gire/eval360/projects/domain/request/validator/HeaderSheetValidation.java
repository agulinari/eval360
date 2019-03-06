package com.gire.eval360.projects.domain.request.validator;

import java.util.function.Function;
import java.util.function.Predicate;

public interface HeaderSheetValidation extends Function<String,ValidationResult>{

	static HeaderSheetValidation fieldIsNotBlank(String message) {
		return holds(field -> field!=null && !field.trim().isEmpty(),message);
	}
	
	static HeaderSheetValidation fieldIsNotNull(String message) {
		return holds(field ->field!=null,message);
	}
	
	static HeaderSheetValidation fieldIsNotEmpty(String message) {
		return holds(field ->!field.trim().isEmpty(),message);
	}
	
	static HeaderSheetValidation holds(Predicate<String> p, String message){
        return field -> p.test(field) ? ValidationResult.valid() : ValidationResult.invalid(message);
    }
	
	default HeaderSheetValidation and(HeaderSheetValidation otherField) {
		return field ->{
            final ValidationResult result = this.apply(field);
            return result.isValid() ? otherField.apply(field) : result;
        };
	}

}
