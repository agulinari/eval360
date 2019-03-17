package com.gire.eval360.projects.service.remote;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gire.eval360.projects.service.remote.dto.users.UserDto;
import com.gire.eval360.projects.service.remote.dto.users.UserResponse;

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
	
	@Override
	public List<UserDto> getUsersByUsername(List<String> usernames) {
		UriComponentsBuilder builder =  UriComponentsBuilder.fromUriString("/usernames");
		builder.queryParam("articleids", String.join(",", usernames));
		URI uri = builder.build().encode().toUri();
		ResponseEntity<List<UserDto>> call = this.restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDto>>() {});

		return call.getBody();
	}
}
