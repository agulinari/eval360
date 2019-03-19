package com.gire.eval360.projects.service.remote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gire.eval360.projects.service.remote.dto.users.UserListDto;
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
	public UserListDto getUsersByUsername(List<String> usernames) {
		String userList = String.join(",", usernames);
		
		ResponseEntity<UserListDto> call = this.restTemplate.getForEntity("/search/usernames?usernames="+userList, UserListDto.class);
		return call.getBody();
	}
}
