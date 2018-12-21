package com.gire.eval360.projects.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gire.eval360.projects.domain.notifications.NotificationFeedbackProviderDto;
import com.gire.eval360.projects.domain.notifications.NotificationReviewerDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Service
public class NotificationFeedBackSender {

	
	private final KafkaSender<String, NotificationFeedbackProviderDto> kafkaNotificationFeedBackSender;
	
	private final KafkaSender<String, NotificationReviewerDto> kafkaNotificationReviewerSender;

	@Value("${app.topic.notificationFP}")
	private String notificationTopicFP;   
	
	@Value("${app.topic.notificationRV}")
	private String notificationTopicRV;   
	
	@Autowired
	public NotificationFeedBackSender(final KafkaSender<String, NotificationFeedbackProviderDto> kafkaNotificationFeedBackSender,
									  final KafkaSender<String, NotificationReviewerDto> kafkaNotificationReviewerSender) {
		this.kafkaNotificationFeedBackSender=kafkaNotificationFeedBackSender;
		this.kafkaNotificationReviewerSender=kafkaNotificationReviewerSender;
	}

	public void sendNotificationFeedBackProvider(NotificationFeedbackProviderDto data){
		
		Mono<SenderRecord<String, NotificationFeedbackProviderDto, NotificationFeedbackProviderDto>> outboundMono = 
				Mono.just(data).map(i -> SenderRecord.create(new ProducerRecord<String, NotificationFeedbackProviderDto>(notificationTopicFP, i), i));


		kafkaNotificationFeedBackSender.send(outboundMono)
		.doOnError(e-> log.error("Sender failed")).subscribe();
	}
	
	public void sendNotificationReviewer(NotificationReviewerDto data){
		
		Mono<SenderRecord<String, NotificationReviewerDto, NotificationReviewerDto>> outboundMono = 
				Mono.just(data).map(i -> SenderRecord.create(new ProducerRecord<String, NotificationReviewerDto>(notificationTopicRV, i), i));


		kafkaNotificationReviewerSender.send(outboundMono)
		.doOnError(e-> log.error("Sender failed")).subscribe();
	}

}
