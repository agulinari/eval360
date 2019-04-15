package com.gire.eval360.notifications.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gire.eval360.notifications.service.remote.domain.Reviewer;

import reactor.core.publisher.Flux;

@Service
public class ProjectServiceRemoteImpl implements ProjectServiceRemote {

	@Autowired
	@Qualifier("projectsClient")
	private WebClient webClient;
	
	@Override
	public Flux<Reviewer> getReviewers(Long idProject, Long idEvaluee) {
		
		Flux<Reviewer> call = this.webClient.get().uri("/"+idProject+"/reviewers/"+idEvaluee)
				.retrieve()
				.bodyToFlux(Reviewer.class);		
		return call;
	}
}
