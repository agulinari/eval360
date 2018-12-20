package com.gire.eval360.notifications.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gire.eval360.notifications.service.remote.domain.UserResponse;

import reactor.core.publisher.Mono;

@Service
public class UserServiceRemoteImpl implements UserServiceRemote{

	@Autowired
	@Qualifier("usersClient")
	private WebClient webClient;
	
	@Override
	public Mono<UserResponse> getUserById(Long idUser) {
		
		Mono<UserResponse> call = this.webClient.get().uri("/"+idUser).retrieve().bodyToMono(UserResponse.class);		
		return call;
	}
	
}
