package com.coxandkings.travel.operations.producer.service;


import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class WorkUnitDispatcher {

    @Autowired
    private KafkaTemplate<String, String> workUnitsKafkaTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkUnitDispatcher.class);

    public boolean dispatch(String topicName,String payload) {
        try {
            SendResult<String, String> sendResult = workUnitsKafkaTemplate.send(topicName,payload).get();
            RecordMetadata recordMetadata = sendResult.getRecordMetadata();
            LOGGER.info("topic = {}, partition = {}, offset = {}, workUnit = {}",
                    recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), payload);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
