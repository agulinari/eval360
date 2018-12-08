package com.gire.eval360.notifications.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.gire.eval360.notifications.domain.NotificationFeedbackProviderDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationReceiver {

    @KafkaListener(topics = "${app.topic.notification}", containerFactory = "kafkaListenerNotificationContainerFactory")
    public void listenTrx(@Payload NotificationFeedbackProviderDto data, @Headers MessageHeaders headers) {
    	System.out.println("received trx='{}'"+ data);

        headers.keySet().forEach(key -> System.out.println("{}: {}" + key + headers.get(key)));

        try {
            System.out.println("Datos recibidos: "+data.getIdUser());
        } catch (Exception e) {
            System.out.println("Error: "+e);
        }
    }


}
