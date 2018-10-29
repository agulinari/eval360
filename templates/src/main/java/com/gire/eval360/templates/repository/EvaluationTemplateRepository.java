package com.gire.eval360.templates.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gire.eval360.templates.domain.EvaluationTemplate;


@RepositoryRestResource
public interface EvaluationTemplateRepository extends JpaRepository<EvaluationTemplate, Long>{

}
