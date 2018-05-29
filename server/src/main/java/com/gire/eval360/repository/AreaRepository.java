package com.gire.eval360.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gire.eval360.domain.Area;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface AreaRepository extends JpaRepository<Area, Long>{

}