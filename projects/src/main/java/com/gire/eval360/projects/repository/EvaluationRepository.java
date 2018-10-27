package com.gire.eval360.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gire.eval360.projects.domain.Evaluation;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface EvaluationRepository extends JpaRepository<Evaluation, Long>{

}
