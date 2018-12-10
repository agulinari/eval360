package com.gire.eval360.notifications.service.remote.domain;

import lombok.Data;

@Data
public class UserResponse {
	
	private String username;
	private String mail;
	private Boolean enabled;

}
