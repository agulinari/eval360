package com.gire.eval360.reports.service.remote;

import com.gire.eval360.reports.service.remote.dto.evaluations.Evaluation;

import reactor.core.publisher.Flux;

public interface EvaluationServiceRemote {

	Flux<Evaluation> getEvaluationsByProjectAndEvaluee(Long projectId, Long evalueeId);
		
}
