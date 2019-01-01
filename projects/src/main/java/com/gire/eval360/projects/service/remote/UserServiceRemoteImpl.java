package com.gire.eval360.projects.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gire.eval360.projects.service.remote.dto.UserResponse;

@Service
public class UserServiceRemoteImpl implements UserServiceRemote {

	@Autowired
	@Qualifier("usersClient")
	private RestTemplate restTemplate;
	
	@Override
	public UserResponse getUserById(Long idUser) {
		
		ResponseEntity<UserResponse> call = this.restTemplate.getForEntity("/"+idUser, UserResponse.class);
		return call.getBody();
	}
}
