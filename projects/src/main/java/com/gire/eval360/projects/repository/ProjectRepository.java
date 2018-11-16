package com.gire.eval360.projects.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.Status;

@RepositoryRestResource(itemResourceRel="project", collectionResourceRel = "project", path = "project")
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project>{
	
	List<Project> findByStatus(@Param("Status") Status status);
	List<Project> findByCreator_IdUser(@Param("ProjectAdmin") Long idUserCreated);
	List<Project> findByStatusAndCreator_IdUser(@Param("Status") Status status,@Param("ProjectAdmin") Long idUserCreated);
		
}
