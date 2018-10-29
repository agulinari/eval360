package com.gire.eval360.templates.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.gire.eval360.templates.domain.EvaluationTemplate;

@Configuration
public class RestConfiguration extends RepositoryRestConfigurerAdapter {
    
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(EvaluationTemplate.class);
       // config.getProjectionConfiguration().addProjection(InlineEmployee.class);
    }
		
}
