package com.gire.eval360.projects.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.gire.eval360.projects.domain.Evaluee;
import com.gire.eval360.projects.domain.FeedbackProvider;
import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.ProjectAdmin;
import com.gire.eval360.projects.domain.Reviewer;
import com.gire.eval360.projects.domain.projection.*;

@Configuration
public class RestConfiguration extends RepositoryRestConfigurerAdapter {
    
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Project.class);
        config.exposeIdsFor(Evaluee.class);
        config.exposeIdsFor(FeedbackProvider.class);
        config.exposeIdsFor(ProjectAdmin.class);
        config.exposeIdsFor(Reviewer.class);
        config.getProjectionConfiguration().addProjection(EvaluatedUser.class).addProjection(FeedbackProviderUser.class)
        .addProjection(ReportTemplate.class).addProjection(ReviewerUser.class).addProjection(TeamMemberUser.class)
        .addProjection(Template.class);
    }
		
}
