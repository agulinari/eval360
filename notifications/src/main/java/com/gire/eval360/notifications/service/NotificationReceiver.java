package com.gire.eval360.notifications.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.gire.eval360.notifications.domain.NotificationFeedbackProviderDto;
import com.gire.eval360.notifications.domain.NotificationReviewerDto;
import com.gire.eval360.notifications.service.remote.UserServiceRemote;
import com.gire.eval360.notifications.service.remote.domain.UserResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NotificationReceiver {
	
    private final EmailService emailService;
	private final UserServiceRemote userService;
	
	@Autowired
	public NotificationReceiver(final UserServiceRemote userService, final EmailService emailService) {
		this.userService = userService;
		this.emailService = emailService;
	}
	
    @KafkaListener(topics = "${app.topic.notificationFP}", containerFactory = "kafkaListenerNotificationFPContainerFactory")
    public void listenTrx(@Payload NotificationFeedbackProviderDto data, @Headers MessageHeaders headers) {
    	log.info("Se ha recibido la notificacion para el provider con los datos: "+ data);
    	Mono<UserResponse> userResponse = this.userService.getUserById(data.getIdUserFeedback());
    
    	Mono<String> resultNotification = userResponse.flatMap(user-> {
    		String to = user.getMail();
			String subject="Feedback pendiente";
    		String url="http://localhost:4200";
    		String link=url+"/main/project-tasks/"+data.getIdProject()+"/evaluation/"+data.getIdEvalueeFP();
    		String message="Se informa que ud tiene un feedback pendiente. Por favor ingrese al link "+link+" para completar el mismo.";
    		try {
    			emailService.sendSimpleMessage(to,subject,message);
    			return Mono.just("Se envio la notificación a "+to);
    		}catch(MailException e) {
    			log.error(e.getMessage());
    		}
    		
    		return Mono.empty();
    		    		
		}).doOnError(e->log.error(e.getMessage()));

    	
    	log.info(resultNotification.block(Duration.ofMillis(30000)));
    }
    
    @KafkaListener(topics = "${app.topic.notificationReport}", containerFactory = "kafkaListenerNotificationRVContainerFactory")
    public void listenTrx(@Payload NotificationReviewerDto data, @Headers MessageHeaders headers) {
    	log.info("Se ha recibido la notificacion para el reviewer con los datos: "+ data);
    	Mono<UserResponse> userResponse = this.userService.getUserById(data.getIdUser());
    
    	Mono<String> resultNotification = userResponse.flatMap(user-> {
    		String to = user.getMail();
			String subject="Proceso de evaluación finalizado";
    		String url="http://localhost:4200";
    		String link=url+"/main/project-tasks/"+data.getIdProject();
    		String message="Se informa que el proceso de evaluación ha terminado. Ingrese al link "+link+" para ver el resultado de la evaluación.";
    		try {
    			emailService.sendSimpleMessage(to,subject,message);
    			return Mono.just("Se envio la notificación a "+to);
    		}catch(MailException e) {
    			log.error(e.getMessage());
    		}
    		
    		return Mono.empty();
    		    		
		}).doOnError(e->log.error(e.getMessage()));

    	
    	log.info(resultNotification.block(Duration.ofMillis(30000)));
    }
    
}
