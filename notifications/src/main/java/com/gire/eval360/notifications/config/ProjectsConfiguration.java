package com.gire.eval360.notifications.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;

@Data
@Configuration
public class ProjectsConfiguration {

	@Autowired
	private ServicesProperties properties;
	
	@Autowired
	private LoadBalancerExchangeFilterFunction lbFunction;
	
	@Bean(name="projectsClient")
	@LoadBalanced
	public WebClient createUsersWebClientRibbon() {
		return WebClient.builder().baseUrl(properties.getProjectsUrl())
			 .filter(lbFunction).build();
	}
	
}
