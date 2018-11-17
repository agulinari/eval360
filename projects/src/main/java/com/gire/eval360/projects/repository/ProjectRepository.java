package com.gire.eval360.projects.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gire.eval360.projects.domain.Project;
import com.gire.eval360.projects.domain.Status;

@RepositoryRestResource
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project>{
	
	List<Project> findByStatus(@Param("Status") Status status);
	
	@Query("select p from Project p where p.status= 'PENDIENTE' "
			+ "and p.id in ( select m.project.id from ProjectAdmin m where m.idUser=:idUser) "
			+ "or p.id in ( select m.project.id from Evaluee m where m.idUser=:idUser)"
			+ "or p.id in ( select m.project.id from FeedbackProvider m where m.idUser=:idUser)"
			+ "or p.id in ( select m.project.id from Reviewer m where m.idUser=:idUser)")
	List<Project> findActiveProjectsByUser(@Param("idUser") Long idUser);
	
	@Query("select p from Project p where p.idEvaluationTemplate=:idTemplate and p.status='PENDIENTE'")
	List<Project> findActiveProjectsByTemplate(@Param("idTemplate") Long idTemplate);
		
}
