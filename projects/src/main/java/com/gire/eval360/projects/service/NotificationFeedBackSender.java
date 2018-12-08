package com.gire.eval360.projects.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gire.eval360.projects.domain.notifications.NotificationFeedbackProviderDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Service
public class NotificationFeedBackSender {

	
	private final KafkaSender<String, NotificationFeedbackProviderDto> kafkaNotificationFeedBackSender;

	@Value("${app.topic.notification}")
	private String notificationTopic;    
	
	@Autowired
	public NotificationFeedBackSender(final KafkaSender<String, NotificationFeedbackProviderDto> kafkaNotificationFeedBackSender) {
		this.kafkaNotificationFeedBackSender=kafkaNotificationFeedBackSender;
	}

	public void sendNotification(NotificationFeedbackProviderDto data){
		
		Mono<SenderRecord<String, NotificationFeedbackProviderDto, NotificationFeedbackProviderDto>> outboundMono = 
				Mono.just(data).map(i -> SenderRecord.create(new ProducerRecord<String, NotificationFeedbackProviderDto>(notificationTopic, i), i));


		kafkaNotificationFeedBackSender.send(outboundMono)
		.doOnError(e-> System.out.println("Sender failed"))  
		// .doOnNext(r -> System.out.printf("Message #%d send response: %s\n", r.correlationMetadata(), r.recordMetadata()))
		.subscribe();
	}

}
