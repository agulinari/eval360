package com.gire.eval360.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.gire.eval360.domain.Address;
import com.gire.eval360.domain.Area;
import com.gire.eval360.domain.ContactInfo;
import com.gire.eval360.domain.Employee;
import com.gire.eval360.domain.Position;

@Configuration
public class RestConfiguration extends RepositoryRestConfigurerAdapter {
    
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Employee.class);
        config.exposeIdsFor(Position.class);
        config.exposeIdsFor(Address.class);
        config.exposeIdsFor(ContactInfo.class);
        config.exposeIdsFor(Area.class);
       // config.getProjectionConfiguration().addProjection(InlineEmployee.class);
    }
		
}
