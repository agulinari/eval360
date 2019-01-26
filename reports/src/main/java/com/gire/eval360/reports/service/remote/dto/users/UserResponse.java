package com.gire.eval360.reports.service.remote.dto.users;

import lombok.Data;

@Data
public class UserResponse {
	
	private String username;
	private String mail;
	private Boolean enabled;

}
