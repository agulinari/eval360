package com.gire.eval360.notifications.controller;

import com.gire.eval360.notifications.dto.UserProvider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProviderNotification {

	private UserProvider user;
	private String resultSend;
	private int status;
}
