package com.gire.eval360.notifications.config;

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
	
	@Value("${projects.url}")
	private String projectsUrl;
	
}
