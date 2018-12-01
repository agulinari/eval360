package com.gire.eval360.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gire.eval360.projects.domain.EvalueeFeedbackProvider;

@RepositoryRestResource(exported = false)
public interface EvalueeFeedbackProviderRepository extends JpaRepository<EvalueeFeedbackProvider, Long>{

	@Query("select efp from EvalueeFeedbackProvider efp where efp.evaluee.id=:idEvaluee and efp.feedbackProvider.id=:idFeedbackProvider")
	EvalueeFeedbackProvider findByEvalueeAndFeedbackProvider(@Param("idEvaluee") Long idEvaluee, @Param("idFeedbackProvider") Long idFeedbackProvider);
}
