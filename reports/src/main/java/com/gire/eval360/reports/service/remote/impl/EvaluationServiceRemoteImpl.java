package com.gire.eval360.reports.service.remote.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gire.eval360.reports.service.remote.EvaluationServiceRemote;
import com.gire.eval360.reports.service.remote.dto.evaluations.Evaluation;

import reactor.core.publisher.Flux;

@Service
public class EvaluationServiceRemoteImpl implements EvaluationServiceRemote {

	@Autowired
	@Qualifier("evaluationsClient")
	private WebClient webClient;
	
	@Override
	public Flux<Evaluation> getEvaluationsByProjectAndEvaluee(Long projectId, Long evalueeId) {
		Flux<Evaluation> call = this.webClient.get().uri("/project/"+projectId+"/evaluee/"+evalueeId).retrieve().bodyToFlux(Evaluation.class);		
		return call;
	}
	
}
