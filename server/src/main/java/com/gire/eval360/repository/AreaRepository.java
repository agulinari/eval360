package com.gire.eval360.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gire.eval360.domain.Area;

@RepositoryRestResource(exported = false)
public interface AreaRepository extends JpaRepository<Area, Long>{

}
