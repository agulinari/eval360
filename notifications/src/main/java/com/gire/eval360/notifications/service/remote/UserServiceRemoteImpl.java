package com.gire.eval360.notifications.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
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
		
		Mono<UserResponse> call = this.webClient.get().uri("/findById").attribute("id", idUser).exchange().flatMap(userResponse-> {
			if(userResponse.statusCode().isError()) {
				return Mono.empty();
			}else {
				Mono<UserResponse> response = userResponse.body(BodyExtractors.toMono(UserResponse.class));
				return response;
			}
		});
		
		return call;
	}
	
}
