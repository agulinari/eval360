package com.gire.eval360.reports.service.remote;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gire.eval360.reports.service.remote.dto.notifications.NotificationReviewerDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Service
public class NotificationSender {
	
	private final KafkaSender<String, NotificationReviewerDto> kafkaNotificationSender;

	@Value("${app.topic.notificationReport}")
	private String notificationTopicReport;   
	
	@Autowired
	public NotificationSender(final KafkaSender<String, NotificationReviewerDto> kafkaNotificationSender) {
		this.kafkaNotificationSender=kafkaNotificationSender;
	}
	
	public void sendNotification(NotificationReviewerDto data){
		
		Mono<SenderRecord<String, NotificationReviewerDto, NotificationReviewerDto>> outboundMono = 
				Mono.just(data).map(i -> SenderRecord.create(new ProducerRecord<String, NotificationReviewerDto>(notificationTopicReport, i), i));


		kafkaNotificationSender.send(outboundMono)
		.doOnError(e-> log.error("Sender failed")).subscribe();
	}

	
}
