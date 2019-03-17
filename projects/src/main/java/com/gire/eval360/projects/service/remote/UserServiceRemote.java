package com.gire.eval360.projects.service.remote;

import java.util.List;

import com.gire.eval360.projects.service.remote.dto.users.UserDto;
import com.gire.eval360.projects.service.remote.dto.users.UserResponse;

public interface UserServiceRemote {
	
	UserResponse getUserById(Long idUser);

	List<UserDto> getUsersByUsername(List<String> usernames);

}
