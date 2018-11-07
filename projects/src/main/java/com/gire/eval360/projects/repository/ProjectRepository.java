package com.gire.eval360.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.User;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface ProjectRepository extends JpaRepository<Project, Long>{
	
		
}
