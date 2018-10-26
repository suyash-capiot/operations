package com.coxandkings.travel.operations.producer.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.producer.bootstrap}")
    private String BOOTSTRAP;
    
    @Value("${kafka.producer.clientId}")
    private String CLIENTID;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
    	return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
    	Map<String, Object> configProps = new HashMap<>();
        configProps.put(
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
          BOOTSTRAP);
        configProps.put(
          ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
          StringSerializer.class);
        configProps.put(
          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
          StringSerializer.class);
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENTID);
        return configProps;
    }

    @Bean
    public KafkaTemplate<String, String> workUnitsKafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate =  new KafkaTemplate<>(producerFactory());
        //kafkaTemplate.setDefaultTopic(kafkaEmailProducerProperties.getTopic());   
        return kafkaTemplate;
    }

}
