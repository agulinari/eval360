package com.gire.eval360.projects.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.gire.eval360.projects.domain.Employee;

@RepositoryRestResource
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	@RestResource(path="findByName", rel="findByName")
	Collection<Employee> findEmployeeByNameContainingIgnoreCaseOrLastnameContainingIgnoreCase(@Param("name") String name, @Param("lastname") String lastname);

	/*@RestResource(path="findAvailable", rel="findAvailable")
	@Query("select e from Employee e where not exists (select 1 from User u where u.employee = e )")
	Collection<Employee> findEmployeesAvailable(@Param("name") String name);
	*/
}
