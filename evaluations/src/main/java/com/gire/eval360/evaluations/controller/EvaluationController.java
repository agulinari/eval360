package com.gire.eval360.evaluations.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gire.eval360.evaluations.domain.Evaluation;
import com.gire.eval360.evaluations.repository.EvaluationRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class EvaluationController {

	@Autowired
	private EvaluationRepository repository;

	@GetMapping("/evaluations")
	public Flux<Evaluation> getAllEvaluations() {
		return repository.findAll();
	}

	@PostMapping("/evaluations")
	public Mono<Evaluation> createEvaluation(@Valid @RequestBody Evaluation evaluation) {
		return repository.save(evaluation);
	}

	@GetMapping("/evaluations/{id}")
	public Mono<ResponseEntity<Evaluation>> getEvaluationById(@PathVariable(value = "id") String evaluationId) {
		return repository.findById(evaluationId).map(savedEval -> ResponseEntity.ok(savedEval))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/evaluations/{id}")
	public Mono<ResponseEntity<Evaluation>> updateTweet(@PathVariable(value = "id") String evaluationId,
			@Valid @RequestBody Evaluation evaluation) {
		return repository.findById(evaluationId).flatMap(existingEval -> {
			//TODO: update existing Eval
			return repository.save(existingEval);
		}).map(updatedEval -> new ResponseEntity<>(updatedEval, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/evaluations/{id}")
	public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable(value = "id") String evaluationId) {

		return repository.findById(evaluationId)
				.flatMap(existingEval -> repository.delete(existingEval)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Evaluations are Sent to the client as Server Sent Events
	@GetMapping(value = "/stream/evaluations", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Evaluation> streamAllTweets() {
		return repository.findAll();
	}

}
