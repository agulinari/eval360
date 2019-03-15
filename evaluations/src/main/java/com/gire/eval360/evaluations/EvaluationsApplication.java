package com.gire.eval360.evaluations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication(exclude = EmbeddedMongoAutoConfiguration.class)
public class EvaluationsApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(EvaluationsApplication.class, args);
	}


}
