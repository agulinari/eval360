package com.gire.eval360.notifications.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.gire.eval360.notifications.domain.NotificationFeedbackProviderDto;
import com.gire.eval360.notifications.domain.NotificationReviewerDto;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.topic.notificationFP}")
    private String notificationTopicFP;
    
    @Value("${app.topic.notificationReport}")
    private String notificationTopicRV;

    @Bean("kafkaListenerNotificationFPContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, NotificationFeedbackProviderDto> kafkaListenerNotificationFPContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NotificationFeedbackProviderDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerNotificationFpFactory());
        return factory;
    }
    
    @Bean("kafkaListenerNotificationRVContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, NotificationReviewerDto> kafkaListenerNotificationRVContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NotificationReviewerDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerNotificationRvFactory());
        return factory;
    }

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "json");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    public ConsumerFactory<String, NotificationFeedbackProviderDto> consumerNotificationFpFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(NotificationFeedbackProviderDto.class));
    }
    
    public ConsumerFactory<String, NotificationReviewerDto> consumerNotificationRvFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(NotificationReviewerDto.class));
    }

}
