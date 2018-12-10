package com.gire.eval360.notifications.service.remote;

import com.gire.eval360.notifications.service.remote.domain.UserResponse;

import reactor.core.publisher.Mono;

public interface UserServiceRemote {
	
	Mono<UserResponse> getUserById(Long idUser);

}
