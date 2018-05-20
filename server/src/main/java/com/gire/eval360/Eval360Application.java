package com.gire.eval360;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
public class Eval360Application {

	public static void main(String[] args) {
		SpringApplication.run(Eval360Application.class, args);
	}
}
