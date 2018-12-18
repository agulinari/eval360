package com.gire.eval360.notifications.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Configuration
public class UsersConfiguration {

	@Autowired
	private ServicesProperties properties;
	
//	@Autowired
//	private LoadBalancerExchangeFilterFunction lbFunction;
	
	@Bean(name="usersClient")
	//@LoadBalanced
	public WebClient createUsersWebClientRibbon() {
		return WebClient.builder().baseUrl(properties.getUsersUrl()).build();
				//.filter(lbFunction).build();
	}
}
