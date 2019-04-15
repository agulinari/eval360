package com.gire.eval360.evaluations.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.reactive.function.client.WebClient;

import com.gire.eval360.evaluations.domain.Evaluation;
import com.gire.eval360.evaluations.domain.notifications.NotificationReviewerDto;
import com.gire.eval360.evaluations.repository.EvaluationRepository;
import com.gire.eval360.evaluations.service.NotificationSender;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class EvaluationController {

	@Autowired
	private EvaluationRepository repository;
	
	@Autowired
	@Qualifier("projectsClient")
	private WebClient webClient;
	
	@Autowired
	private NotificationSender notificationSender;
	

	@GetMapping("/evaluations")
	public Flux<Evaluation> getAllEvaluations() {
		return repository.findAll();
	}

	@PostMapping("/evaluations")
	public Mono<Evaluation> createEvaluation(@Valid @RequestBody Evaluation evaluation) {
		
		return repository.save(evaluation)
		.doOnSuccess(e -> {
			NotificationReviewerDto data = new NotificationReviewerDto();
			data.setIdFeedbackProvider(e.getIdFeedbackProvider());
			data.setIdEvaluee(e.getIdEvaluee());
			data.setIdProject(e.getIdProject());
			data.setIdTemplate(e.getIdTemplate());
			notificationSender.sendNotificationReviewer(data).subscribe();
		});
	}

	@GetMapping("/evaluations/{id}")
	public Mono<ResponseEntity<Evaluation>> getEvaluationById(@PathVariable(value = "id") String evaluationId) {
		return repository.findById(evaluationId).map(savedEval -> ResponseEntity.ok(savedEval))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/evaluations/project/{id}")
	public Flux<Evaluation> getEvaluationsByProject(@PathVariable(value = "id") Long projectId) {
		return repository.findByIdProject(projectId);
	}
	
	@GetMapping("/evaluations/project/{id}/feedbackProvider/{idFp}")
	public Flux<Evaluation> getEvaluationsByProjectAndFeedbackProvider(@PathVariable(value = "id") Long projectId, @PathVariable(value="idFp") Long feedbackProviderId) {
		return repository.findByIdProjectAndIdFeedbackProvider(projectId, feedbackProviderId);
	}
	
	@GetMapping("/evaluations/project/{id}/evaluee/{idEvaluee}")
	public Flux<Evaluation> getEvaluationsByProjectAndEvaluee(@PathVariable(value = "id") Long projectId, @PathVariable(value="idEvaluee") Long evalueeId) {
		return repository.findByIdProjectAndIdEvaluee(projectId, evalueeId);
	}


	@PutMapping("/evaluations/{id}")
	public Mono<ResponseEntity<Evaluation>> updateEvaluation(@PathVariable(value = "id") String evaluationId,
			@Valid @RequestBody Evaluation evaluation) {
		return repository.findById(evaluationId).flatMap(existingEval -> {
			existingEval.setIdEvaluee(evaluation.getIdEvaluee());
			existingEval.setIdFeedbackProvider(evaluation.getIdFeedbackProvider());
			existingEval.setIdProject(evaluation.getIdProject());
			return repository.save(existingEval);
		}).map(updatedEval -> new ResponseEntity<>(updatedEval, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/evaluations/{id}")
	public Mono<ResponseEntity<Void>> deleteEvaluation(@PathVariable(value = "id") String evaluationId) {

		return repository.findById(evaluationId)
				.flatMap(existingEval -> repository.delete(existingEval)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Evaluations are Sent to the client as Server Sent Events
	@GetMapping(value = "/stream/evaluations", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Evaluation> streamAllEvaluation() {
		return repository.findAll();
	}

}
