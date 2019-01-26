package com.gire.eval360.reports.configuration.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.gire.eval360.reports.configuration.ServicesProperties;


@Configuration
public class UsersConfiguration {

	@Autowired
	private ServicesProperties properties;
	
	@Autowired
	private LoadBalancerExchangeFilterFunction lbFunction;
	
	@Bean(name="usersClient")
	@LoadBalanced
	public WebClient webClient(RestTemplateBuilder builder) {
		return WebClient.builder().baseUrl(properties.getUsersUrl())
				 .filter(lbFunction).build();
	}
	
}
