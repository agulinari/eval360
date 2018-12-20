package com.gire.eval360.notifications.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;

import com.gire.eval360.notifications.domain.EvaluationDto;

import reactor.core.publisher.Flux;

public class EvaluationServiceRemoteImpl implements EvaluationServiceRemote{

	@Autowired
	@Qualifier("evaluationClient")
	private WebClient webClient;
	
	@Override
	public Flux<EvaluationDto> getEvaluationsByProjectAndEvaluee(Long projectId, Long evalueeId) {
		Flux<EvaluationDto> call = this.webClient.get().uri("/project/"+projectId+"/evaluee/"+evalueeId).retrieve().bodyToFlux(EvaluationDto.class);		
		return call;
	}

}
