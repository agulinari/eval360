package com.gire.eval360.notifications.service.remote.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
	
	private String username;
	private String mail;
	private Boolean enabled;

}
