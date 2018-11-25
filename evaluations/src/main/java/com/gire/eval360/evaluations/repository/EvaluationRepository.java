package com.gire.eval360.evaluations.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.gire.eval360.evaluations.domain.Evaluation;

import reactor.core.publisher.Mono;

public interface EvaluationRepository extends ReactiveCrudRepository<Evaluation, Long> {

	Mono<Evaluation> findById(String evaluationId);

}
