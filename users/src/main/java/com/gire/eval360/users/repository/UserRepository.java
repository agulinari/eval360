package com.gire.eval360.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.gire.eval360.users.domain.User;


@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long>{

	List<User> findByUsername(@Param("name") String username);

	@RestResource(path = "usernameIgnoreCase", rel = "usernameIgnoreCase")
	List<User> findByUsernameContainingIgnoreCase(@Param("name") String name);

}
