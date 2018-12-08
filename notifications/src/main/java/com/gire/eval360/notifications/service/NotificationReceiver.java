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
        log.debug("received trx='{}'", data);

        headers.keySet().forEach(key -> log.debug("{}: {}", key, headers.get(key)));

        try {
            System.out.println("Datos recibidos: "+data.getIdUser());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


}
