package com.gire.eval360.notifications.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Singular;

@Data
@AllArgsConstructor
public class NotificationProvidersResponse {
	
	private @Singular List<UserProviderNotification> notificationsResult;
		
}
