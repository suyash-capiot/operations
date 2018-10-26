package com.coxandkings.travel.operations.consumer;

import com.coxandkings.travel.operations.consumer.factory.MdmListenerFactory;
import com.coxandkings.travel.operations.consumer.factory.MngPrdtUpdateListenerFactory;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class MDMMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MDMMessageConsumer.class);

    @Value("${kafka.consumer.MDM_PRODUCT_TOPIC}")
    private String MDM_PRODUCT_TOPIC;

    @Value("${kafka.consumer.MDM_PRODUCT_MESSAGE_CONSUMER_GROUP}")
    private String MDM_PRODUCT_MESSAGE_CONSUMER_GROUP;

    @Value("${kafka.consumer.MDM_SUPPLIER_TOPIC}")
    private String MDM_SUPPLIER_TOPIC;

    @Value("${kafka.consumer.MDM_PRODUCT_MESSAGE_CONSUMER_GROUP}")
    private String MDM_SUPPLIER_MESSAGE_CONSUMER_GROUP;

    @Autowired
    MngPrdtUpdateListenerFactory mngPrdtUpdateListenerFactory;

    @Autowired
    private MdmListenerFactory mdmListenerFactory;

    @KafkaListener(id = "MDMProductMessageConsumer", topics = "${kafka.consumer.MDM_PRODUCT_TOPIC}", groupId = "${kafka.consumer.MDM_PRODUCT_MESSAGE_CONSUMER_GROUP}")
    public void onProductChangeReceiving(String payload, @Header(KafkaHeaders.OFFSET) Integer offset,
                                         @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws JSONException {
        log.info("\n***Received new message from Topic " + MDM_PRODUCT_TOPIC + " using consumer group: " + MDM_PRODUCT_MESSAGE_CONSUMER_GROUP +
                        "\nProcessing topic = {}, partition = {}, offset = {}, payload = {} ***",
                topic, partition, offset, payload);
        //  mdmListenerFactory.processSupplier(payload);
        //  mdmListenerFactory.processSupplier(payload);
        mngPrdtUpdateListenerFactory.processMngPrdtUpdate(payload);
    }


    @KafkaListener(id = "MDMSupplierMessageConsumer", topics = "${kafka.consumer.MDM_SUPPLIER_TOPIC}", groupId = "${kafka.consumer.MDM_SUPPLIER_MESSAGE_CONSUMER_GROUP}")
    public void onSupplierChangeReceiving(String payload, @Header(KafkaHeaders.OFFSET) Integer offset,
                            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws JSONException {
        log.info("\n***Received new message from Topic " + MDM_SUPPLIER_TOPIC + " using consumer group: " + MDM_SUPPLIER_MESSAGE_CONSUMER_GROUP +
                        "\nProcessing topic = {}, partition = {}, offset = {}, payload = {} ***",
                topic, partition, offset, payload);
        mdmListenerFactory.processSupplier(payload);
    }
}
