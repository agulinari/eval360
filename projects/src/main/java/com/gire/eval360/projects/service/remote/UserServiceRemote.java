package com.gire.eval360.projects.service.remote;

import com.gire.eval360.projects.service.remote.dto.users.UserResponse;

public interface UserServiceRemote {
	
	UserResponse getUserById(Long idUser);

}
