package com.coxandkings.travel.operations.consumer;

import com.coxandkings.travel.operations.consumer.factory.EmailListenerFactory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import javax.jcr.RepositoryException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;

@Service
public class EmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    @Autowired
    EmailListenerFactory emailListenerFactory;

    @KafkaListener(topics = "emailTopic")
    public void onReceiving(String payload, @Header(KafkaHeaders.OFFSET) Integer offset,
                            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws IOException, SQLException, OperationException, JSONException, MessagingException, RepositoryException {
        log.info("\n***Received new message from emailTopic Topic::: Processing topic = {}, partition = {}, offset = {}, workUnit = {}",
                topic, partition, offset, payload);

        emailListenerFactory.processEmail(payload);
    }
}
