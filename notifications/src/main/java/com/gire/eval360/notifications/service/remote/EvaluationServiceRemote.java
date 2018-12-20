package com.gire.eval360.notifications.service.remote;

import com.gire.eval360.notifications.domain.EvaluationDto;

import reactor.core.publisher.Flux;

public interface EvaluationServiceRemote {
	
	public Flux<EvaluationDto> getEvaluationsByProjectAndEvaluee(Long projectId, Long evalueeId);

}
