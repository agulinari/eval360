package com.gire.eval360.notifications.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationProvidersRequest {
	
	private Long idEvalueeFP;
	private Long idProject;
	private @Singular List<UserProvider> providers;
}
