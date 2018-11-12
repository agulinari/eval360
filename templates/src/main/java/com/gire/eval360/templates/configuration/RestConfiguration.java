package com.gire.eval360.templates.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.gire.eval360.templates.domain.EvaluationTemplate;
import com.gire.eval360.templates.domain.ItemTemplate;
import com.gire.eval360.templates.domain.Section;

@Configuration
public class RestConfiguration extends RepositoryRestConfigurerAdapter {
    
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(EvaluationTemplate.class);
        config.exposeIdsFor(Section.class);
        config.exposeIdsFor(ItemTemplate.class);
       // config.getProjectionConfiguration().addProjection(InlineEmployee.class);
    }
		
}
