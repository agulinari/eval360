package com.gire.eval360.notifications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.gire.eval360.notifications.domain.EvaluationDto;
import com.gire.eval360.notifications.domain.NotificationFeedbackProviderDto;
import com.gire.eval360.notifications.service.remote.EvaluationServiceRemote;
import com.gire.eval360.notifications.service.remote.UserServiceRemote;
import com.gire.eval360.notifications.service.remote.domain.UserResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NotificationReceiver {
	
    private final EmailService emailService;
	private final UserServiceRemote userService;
	private final EvaluationServiceRemote evaluationService;
	
	@Autowired
	public NotificationReceiver(final UserServiceRemote userService, final EmailService emailService, final EvaluationServiceRemote evaluationService) {
		this.userService = userService;
		this.emailService = emailService;
		this.evaluationService = evaluationService;
	}
	
    @KafkaListener(topics = "${app.topic.notification}", containerFactory = "kafkaListenerNotificationContainerFactory")
    public void listenTrx(@Payload NotificationFeedbackProviderDto data, @Headers MessageHeaders headers) {
    	System.out.println("received trx='{}'"+ data);
    	Mono<UserResponse> userResponse = this.userService.getUserById(data.getIdUserFeedback());
//    	Flux<EvaluationDto> evaluationResponse = this.evaluationService.getEvaluationsByProjectAndEvaluee(data.getIdProject(), data.getIdUserEvaluee());
//    	
//    	Mono<Object> monoResponse = evaluationResponse..suscriber();
//    	
//    	(user->{
//    		
//    		String to=user.getMail();
//    		
//    		 evaluationResponse.flatMap(eval-> {
//    			String subject="Feedback pendiente";
//        		String url="http://localhost:4200";
//        		String link=url+"/main/project-tasks/"+data.getIdProject()+"/evaluation/"+eval.getId();
//        		String message="Se informa que ud tiene un feedback pendiente. Por favor ingrese al link "+link+" para completar el mismo.";
//        		try {
//        			emailService.sendSimpleMessage(to,subject,message);
//        			return "Se envio la notificación a "+to;
//        		}catch(MailException e) {
//        			return e;
//        		}
//    		});
//    		
//    	}).doOnError(e->log.error(e.getMessage()));
    	
//    	log.info(monoResponse.block(Duration.ofMillis(10000)));
    }
    
}
