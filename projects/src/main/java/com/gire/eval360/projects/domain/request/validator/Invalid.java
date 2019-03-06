package com.gire.eval360.projects.domain.request.validator;

import java.util.Optional;

final class Invalid implements ValidationResult {

    private final String reason;

    Invalid(String reason){
        this.reason = reason;
    }

    public boolean isValid(){
        return false;
    }

    public Optional<String> getReason(){
        return Optional.of(reason);
    }

    // equals and hashCode on field reason
}
