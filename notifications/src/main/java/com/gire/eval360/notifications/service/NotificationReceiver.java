package com.gire.eval360.notifications.service;

import java.io.UnsupportedEncodingException;
import java.time.Duration;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
	private final TemplateEngine templateMail;
	
	@Autowired
	public NotificationReceiver(final UserServiceRemote userService, final EmailService emailService,final TemplateEngine templateMail) {
		this.userService = userService;
		this.emailService = emailService;
		this.templateMail = templateMail;
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
    		String message="Se informa que ud tiene un feedback pendiente. Ingrese al siguiente link para completarlo.";
    		Context context = new Context();
            context.setVariable("data", user);
            context.setVariable("message", message);
            context.setVariable("linkRef", link);
            String renderedHtmlContent = templateMail.process("NotificationMail", context);
    		try {
    			emailService.sendSimpleMessage(to,subject,renderedHtmlContent);
    			return Mono.just("Se envio la notificación a "+to);
    		} catch (MessagingException | UnsupportedEncodingException e) {
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
    		String message="Se informa que el proceso de evaluación ha terminado. Ingrese al siguiente link para ver el resultado de la evaluación.";
    		Context context = new Context();
            context.setVariable("data", user);
            context.setVariable("message", message);
            context.setVariable("linkRef", link);
            String renderedHtmlContent = templateMail.process("NotificationMail", context);
    		try {
    			emailService.sendSimpleMessage(to,subject,renderedHtmlContent);
    			return Mono.just("Se envio la notificación a "+to);
    		} catch (MessagingException | UnsupportedEncodingException e) {
				log.error(e.getMessage());
    		}
    		
    		return Mono.empty();
    		    		
		}).doOnError(e->log.error(e.getMessage()));

    	
    	log.info(resultNotification.block(Duration.ofMillis(30000)));
    }
    
}
