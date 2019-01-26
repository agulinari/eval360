package com.gire.eval360.reports.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ServicesProperties {

	@Value("${users.url}")
	private String usersUrl;

	@Value("${evaluations.url}")
	private String evaluationsUrl;
	
	@Value("${templates.url}")
	private String templatesUrl;
	
	@Value("${projects.url}")
	private String projectsUrl;
	
}
