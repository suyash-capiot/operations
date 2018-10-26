package com.coxandkings.travel.operations.consumer;

import com.coxandkings.travel.operations.consumer.factory.BookingListenerFactory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BEMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(BEMessageConsumer.class);

    @Autowired
    BookingListenerFactory bookingListenerFactory;
    @Value("${kafka.consumer.BE_TOPIC}")
    private String beTopic;
    @Value("${kafka.consumer.BE_MESSAGE_CONSUMER_GROUP}")
    private String beConsumerMessageGroup;

    //@KafkaListener(id = "BEMessageConsumer", topics = "${kafka.consumer.BE_TOPIC}", groupId = "${kafka.consumer.BE_MESSAGE_CONSUMER_GROUP}")
    @KafkaListener(id = "BEMessageConsumer", topics = "${kafka.consumer.BE_TOPIC}", groupId = "${BE_CONSUMER_GROUP_ID}")
    public void onReceivingBEMessage(String payload, @Header(KafkaHeaders.OFFSET) Integer offset,
                                     @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws IOException, OperationException {
        log.info("\n***Received new message from Topic " + beTopic + " using consumer group: " + beConsumerMessageGroup +
                        "\nProcessing topic = {}, partition = {}, offset = {}, payload = {} ***",
                topic, partition, offset, payload);
        bookingListenerFactory.processBooking(payload);
    }
}
