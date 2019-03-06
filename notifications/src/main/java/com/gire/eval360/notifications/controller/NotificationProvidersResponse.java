package com.gire.eval360.notifications.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationProvidersResponse {
	
	private @Singular("notificationResult") List<UserProviderNotification> notificationsResult;
		
}
