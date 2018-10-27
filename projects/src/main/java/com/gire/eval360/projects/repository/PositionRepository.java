package com.gire.eval360.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gire.eval360.projects.domain.Position;

@RepositoryRestResource(exported = false)
public interface PositionRepository extends JpaRepository<Position, Long>{

}
