package com.gire.eval360.projects.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Data;

@Data
@Configuration
public class EvaluationsConfiguration {
	
	@Autowired
	private ServicesProperties properties;

	@Bean(name="evaluationsClient")
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.rootUri(properties.getEvaluationsUrl()).build();
	}

}
