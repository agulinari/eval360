package com.gire.eval360.templates.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.gire.eval360.templates.domain.EvaluationTemplate;


@RepositoryRestResource
public interface EvaluationTemplateRepository extends CrudRepository<EvaluationTemplate, Long>{

	@RestResource(path="titleContains", rel="titleContains")
	public Page<EvaluationTemplate> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable p);
	
}
