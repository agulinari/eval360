package com.gire.eval360.configuration;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
    	return null;
        // Can use Spring Security to return currently logged in user
     //   return Optional.of(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }
}