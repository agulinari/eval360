package com.gire.eval360.evaluations.controller;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.gire.eval360.evaluations.domain.Evaluation;
import com.gire.eval360.evaluations.repository.EvaluationRepository;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EvaluationControllerTests {


	@Autowired
	private WebTestClient webTestClient;

	@Autowired
    EvaluationRepository repository;
	@Ignore
	@Test
	public void testCreateEvaluation() {
		Evaluation evaluation = new Evaluation();

		webTestClient.post().uri("/evaluations")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(evaluation), Evaluation.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
                .jsonPath("$.id").isNotEmpty();
	}

	@Test
    public void testGetAllEvaluations() {
	    webTestClient.get().uri("/evaluations")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Evaluation.class);
    }

    @Test
    public void testGetSingleEvaluation() {
    	Evaluation evaluation = repository.save(new Evaluation()).block();

        webTestClient.get()
                .uri("/evaluations/{id}", Collections.singletonMap("id", evaluation.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testUpdateEvaluation() {
    	Evaluation evaluation = repository.save(new Evaluation()).block();

    	Evaluation newEvaluation = new Evaluation();
    	newEvaluation.setIdEvaluee(Long.valueOf(2));

        webTestClient.put()
                .uri("/evaluations/{id}", Collections.singletonMap("id", evaluation.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newEvaluation), Evaluation.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.idEvaluee").isEqualTo(Long.valueOf(2));
    }

    @Test
    public void testDeleteEvaluation() {
    	Evaluation evaluation = repository.save(new Evaluation()).block();

	    webTestClient.delete()
                .uri("/evaluations/{id}", Collections.singletonMap("id",  evaluation.getId()))
                .exchange()
                .expectStatus().isOk();
    }
    
}