package com.gire.eval360.reports.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.gire.eval360.reports.domain.ReportData;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReportRepository extends ReactiveCrudRepository<ReportData, String> {

	Mono<ReportData> findById(String evaluationId);
	
	Flux<ReportData> findByIdProject(Long idProject);
	
	Mono<ReportData> findByIdEvaluee(Long idEvaluee);

}
