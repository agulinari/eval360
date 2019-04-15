package com.gire.eval360.projects.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.gire.eval360.projects.domain.notifications.NotificationFeedbackProviderDto;
import com.gire.eval360.projects.domain.notifications.NotificationReviewerDto;

import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;


@Configuration
public class KafkaConfiguration {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	
    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;
	
    @Value("${spring.kafka.properties.sasl.jaas.config}")
    private String saslConfig;
    
    @Value("${spring.kafka.sslEnabled}")
    private boolean sslEnabled;
    
	@Bean
	public Map<String, Object> producerConfig() {

		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        if (sslEnabled) {
            props.put("security.protocol", "SASL_SSL");
            props.put("sasl.mechanism", "SCRAM-SHA-256");
            props.put("sasl.jaas.config", saslConfig);
        }
		return props;
	}

	@Bean
	public SenderOptions<String, NotificationFeedbackProviderDto> senderNotificationOptionsFP() {

		SenderOptions<String, NotificationFeedbackProviderDto> senderOptions =
				SenderOptions.<String, NotificationFeedbackProviderDto>create(producerConfig())       
				.maxInFlight(1024);   
		return senderOptions;
	}

	@Bean
	public KafkaSender<String, NotificationFeedbackProviderDto> senderNotificationFactoryFP() {
		KafkaSender<String, NotificationFeedbackProviderDto> sender = KafkaSender.create(senderNotificationOptionsFP());
		return sender;
	}
	
	@Bean
	public SenderOptions<String, NotificationReviewerDto> senderNotificationOptionsRV() {

		SenderOptions<String, NotificationReviewerDto> senderOptions =
				SenderOptions.<String, NotificationReviewerDto>create(producerConfig())       
				.maxInFlight(1024);   
		return senderOptions;
	}

	@Bean
	public KafkaSender<String, NotificationReviewerDto> senderNotificationFactoryRV() {
		KafkaSender<String, NotificationReviewerDto> sender = KafkaSender.create(senderNotificationOptionsRV());
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
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        if (sslEnabled) {
            props.put("security.protocol", "SASL_SSL");
            props.put("sasl.mechanism", "SCRAM-SHA-256");
            props.put("sasl.jaas.config", saslConfig);
        }
        return props;
    }


    public ConsumerFactory<String, NotificationReviewerDto> consumerNotificationRvFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(NotificationReviewerDto.class));
    }


}
