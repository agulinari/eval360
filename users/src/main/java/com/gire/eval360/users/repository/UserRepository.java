package com.gire.eval360.users.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.gire.eval360.users.domain.User;


@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long>{

	List<User> findByUsername(@Param("name") String username);

	@RestResource(path = "usernameContains", rel = "usernameContains")
	Page<User> findByUsernameContainingIgnoreCase(@Param("name") String name, Pageable p);
	
	@RestResource(path = "usernames", rel = "usernames")
	@Query("select u from User u where upper(u.username) in :usernames")
	List<User> findByUsernames(@Param("usernames") List<String> usernames);

}
