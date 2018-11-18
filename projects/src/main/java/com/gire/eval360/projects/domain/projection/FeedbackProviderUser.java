package com.gire.eval360.projects.domain.projection;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.gire.eval360.projects.domain.FeedbackProvider;
import com.gire.eval360.projects.domain.Project;

@Projection(name = "feedBackProviderUsers", types = { Project.class }) 
public interface FeedbackProviderUser {

	List<FeedbackProvider> getFeedbackProviders();
	
}
