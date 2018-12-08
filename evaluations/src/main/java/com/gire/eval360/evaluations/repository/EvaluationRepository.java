package com.gire.eval360.evaluations.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.gire.eval360.evaluations.domain.Evaluation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EvaluationRepository extends ReactiveCrudRepository<Evaluation, String> {

	Mono<Evaluation> findById(String evaluationId);
	
	Flux<Evaluation> findByIdProject(Long idProject);
	
	Flux<Evaluation> findByIdProjectAndIdFeedbackProvider(Long idProject, Long idFeedbackProvider);
	
	Flux<Evaluation> findByIdProjectAndIdEvaluee(Long idProject, Long idEvaluee);

}
