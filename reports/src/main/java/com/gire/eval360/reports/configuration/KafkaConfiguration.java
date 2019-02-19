package com.gire.eval360.reports.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.gire.eval360.reports.service.remote.dto.notifications.NotificationReviewerDto;

import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;


@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.topic.notificationRV}")
    private String notificationTopicRV;
    
	@Bean
	public Map<String, Object> producerConfig() {

		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

		return props;
	}
	
	@Bean
	public SenderOptions<String, NotificationReviewerDto> senderNotificationOptions() {

		SenderOptions<String, NotificationReviewerDto> senderOptions =
				SenderOptions.<String, NotificationReviewerDto>create(producerConfig())       
				.maxInFlight(1024);   
		return senderOptions;
	}

	@Bean
	public KafkaSender<String, NotificationReviewerDto> senderNotificationFactory() {
		KafkaSender<String, NotificationReviewerDto> sender = KafkaSender.create(senderNotificationOptions());
		return sender;
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


    public ConsumerFactory<String, NotificationReviewerDto> consumerNotificationRvFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(NotificationReviewerDto.class));
    }

}