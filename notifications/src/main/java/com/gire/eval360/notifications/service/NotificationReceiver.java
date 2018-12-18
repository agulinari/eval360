package com.gire.eval360.notifications.service;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.gire.eval360.notifications.domain.EvaluationStatus;
import com.gire.eval360.notifications.domain.NotificationFeedbackProviderDto;
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
	
    @KafkaListener(topics = "${app.topic.notification}", containerFactory = "kafkaListenerNotificationContainerFactory")
    public void listenTrx(@Payload NotificationFeedbackProviderDto data, @Headers MessageHeaders headers) {
    	System.out.println("received trx='{}'"+ data);
    	Mono<UserResponse> userResponse = this.userService.getUserById(data.getIdUserFeedback());
    	
    	Mono<Object> response=userResponse.flatMap(user->{
    		String to=user.getMail();
    		String subject="Feedback pendiente";
    		String url="http://localhost:4200";
    		String link=url+"/main/project-tasks/"+data.getIdProject()+"/evaluation/"+data.getIdUserEvaluee();
    		String message="Se informa que ud tiene un feedback pendiente. Por favor ingrese al link "+link+" para completar el mismo.";
    		try {
    			emailService.sendSimpleMessage(to,subject,message);
    			return Mono.empty();
    		}catch(MailException e) {
    			return Mono.error(e);
    		}
    	});
        
    	System.out.println("Resultado: "+response.toString());
    }
    
}
