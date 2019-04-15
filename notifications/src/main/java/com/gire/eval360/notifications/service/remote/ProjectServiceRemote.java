package com.gire.eval360.notifications.service.remote;

import com.gire.eval360.notifications.service.remote.domain.Reviewer;

import reactor.core.publisher.Flux;

public interface ProjectServiceRemote {
	
	Flux<Reviewer> getReviewers(Long projectId, Long evalueeId);

}
