package com.gire.eval360.reports.service.remote;

import com.gire.eval360.reports.service.remote.dto.users.UserResponse;

import reactor.core.publisher.Mono;

public interface UserServiceRemote {
	
	Mono<UserResponse> getUserById(Long idUser);

}
