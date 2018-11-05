package com.gire.eval360.projects.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RestConfiguration extends RepositoryRestConfigurerAdapter {
    
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
     
       // config.getProjectionConfiguration().addProjection(InlineEmployee.class);
    }
		
}
