package com.gire.eval360.evaluations.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gire.eval360.evaluations.domain.notifications.NotificationReviewerDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

@Slf4j
@Service
public class NotificationSender {
	
	private final KafkaSender<String, NotificationReviewerDto> kafkaNotificationReviewerSender;

	@Value("${app.topic.notificationRV}")
	private String notificationTopicRV;   
	
	@Autowired
	public NotificationSender(final KafkaSender<String, NotificationReviewerDto> kafkaNotificationReviewerSender) {
		this.kafkaNotificationReviewerSender=kafkaNotificationReviewerSender;
	}

	
	public Flux<SenderResult<NotificationReviewerDto>> sendNotificationReviewer(NotificationReviewerDto data){
		
		Mono<SenderRecord<String, NotificationReviewerDto, NotificationReviewerDto>> outboundMono = 
				Mono.just(data).map(i -> SenderRecord.create(new ProducerRecord<String, NotificationReviewerDto>(notificationTopicRV, i), i));


		return kafkaNotificationReviewerSender.send(outboundMono)
		.doOnError(e-> log.error("Sender failed"))
		.doOnNext(result -> {
			NotificationReviewerDto dto = result.correlationMetadata();
			log.trace("Notification successfully sent {} in Kafka", dto);
		});
	}

}
