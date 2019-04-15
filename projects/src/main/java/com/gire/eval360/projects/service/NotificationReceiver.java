package com.gire.eval360.projects.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.gire.eval360.projects.domain.notifications.NotificationReviewerDto;
import com.gire.eval360.projects.domain.request.ReportFeedbackRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationReceiver {
	
	private final ProjectService projectService;
	
	@Autowired
	public NotificationReceiver(final ProjectService projectService) {
		this.projectService = projectService;
	}
	

    @KafkaListener(topics = "${app.topic.notificationRV}", containerFactory = "kafkaListenerNotificationRVContainerFactory")
    public void listenTrx(@Payload NotificationReviewerDto data, @Headers MessageHeaders headers) {
    	log.info("Se ha recibido una notificación de evaluacion finalizada: "+ data);
    	if (data.getIdFeedbackProvider().longValue() == 0) { 
    		// Es una notificacion de feedback completo, se ignora
    		return;
    	}
    	ReportFeedbackRequest request = ReportFeedbackRequest.builder()
    			.idEvaluee(data.getIdEvaluee())
    			.idFeedbackProvider(data.getIdFeedbackProvider())
    			.build();
    	projectService.reportFeedback(request);
    		 		   	
    }
    
}