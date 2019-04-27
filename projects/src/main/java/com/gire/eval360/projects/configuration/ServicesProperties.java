package com.gire.eval360.projects.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ServicesProperties {

	@Value("${users.url}")
	private String usersUrl;
	
	@Value("${templates.url}")
	private String templatesUrl;

	@Value("${evaluations.url}")
	public String evaluationsUrl;

	
}
