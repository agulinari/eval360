package com.gire.eval360.reports.service.remote.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gire.eval360.reports.service.remote.TemplateServiceRemote;
import com.gire.eval360.reports.service.remote.dto.templates.EvaluationTemplate;

import reactor.core.publisher.Mono;

@Service
public class TemplateServiceRemoteImpl implements TemplateServiceRemote {

	@Autowired
	@Qualifier("templatesClient")
	private WebClient webClient;
	
	@Override
	public Mono<EvaluationTemplate> getTemplateById(Long idTemplate) {
		
		Mono<EvaluationTemplate> call = this.webClient.get().uri("/"+idTemplate).retrieve().bodyToMono(EvaluationTemplate.class);		
		return call;
	}
}
