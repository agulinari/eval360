package com.gire.eval360.users.configuration;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@ActiveProfiles("dev")
@Configuration
public class DatabaseConfiguration {

	  @Value("${spring.datasource.url}")
	  private String dbUrl;

	  @Bean
	  public DataSource dataSource() {
	      HikariConfig config = new HikariConfig();
	      config.setJdbcUrl(dbUrl);
	      return new HikariDataSource(config);
	  }
	  
}
