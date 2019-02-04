package com.gire.eval360.notifications.controller;

import com.gire.eval360.notifications.dto.UserProvider;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProviderNotification {

	private UserProvider user;
	private String resultSend;
	private int status;
}
