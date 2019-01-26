package com.gire.eval360.reports.service.remote;

import com.gire.eval360.reports.service.remote.dto.templates.EvaluationTemplate;

import reactor.core.publisher.Mono;

public interface TemplateServiceRemote {
	
	Mono<EvaluationTemplate> getTemplateById(Long idTemplate);

}
