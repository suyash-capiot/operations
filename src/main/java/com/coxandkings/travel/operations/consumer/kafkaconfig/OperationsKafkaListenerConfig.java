package com.coxandkings.travel.operations.consumer.kafkaconfig;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class OperationsKafkaListenerConfig {

    private static final Logger logger = LoggerFactory.getLogger(OperationsKafkaListenerConfig.class);

    @Value("${kafka.consumer.bootstrap}")
    private String bootstrap;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        logger.info("*** Initializing OperationsKafkaListenerConfig.kafkaListenerContainerFactory() ***");
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(consumerFactory());
        logger.info("*** Exit OperationsKafkaListenerConfig.kafkaListenerContainerFactory() ***");
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        logger.info("*** Initializing OperationsKafkaListenerConfig.consumerFactory() ***");
        return new DefaultKafkaConsumerFactory<>(getBEConsumerProps(), stringKeyDeserializer(), stringKeyDeserializer());
    }

    @Bean
    public Map<String, Object> getBEConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.getGroup());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "OPERATIONS_KAFKA_CONSUMER_GROUP");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        return props;
    }

    @Bean
    public Deserializer stringKeyDeserializer() {
        return new StringDeserializer();
    }
}