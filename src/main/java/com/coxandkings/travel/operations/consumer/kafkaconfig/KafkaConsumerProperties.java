package com.coxandkings.travel.operations.consumer.kafkaconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.consumer")
public class KafkaConsumerProperties {

    private String bootstrap;
    private String BE_TOPIC;
    private String MDM_TOPIC;
    private String BE_MESSAGE_CONSUMER_GROUP;
    private String MDM_MESSAGE_CONSUMER_GROUP;

    public String getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(String bootstrap) {
        this.bootstrap = bootstrap;
    }

    public String getBE_TOPIC() {
        return BE_TOPIC;
    }

    public void setBE_TOPIC(String BE_TOPIC) {
        this.BE_TOPIC = BE_TOPIC;
    }

    public String getMDM_TOPIC() {
        return MDM_TOPIC;
    }

    public void setMDM_TOPIC(String MDM_TOPIC) {
        this.MDM_TOPIC = MDM_TOPIC;
    }

    public String getBE_MESSAGE_CONSUMER_GROUP() {
        return BE_MESSAGE_CONSUMER_GROUP;
    }

    public void setBE_MESSAGE_CONSUMER_GROUP(String BE_MESSAGE_CONSUMER_GROUP) {
        this.BE_MESSAGE_CONSUMER_GROUP = BE_MESSAGE_CONSUMER_GROUP;
    }

    public String getMDM_MESSAGE_CONSUMER_GROUP() {
        return MDM_MESSAGE_CONSUMER_GROUP;
    }

    public void setMDM_MESSAGE_CONSUMER_GROUP(String MDM_MESSAGE_CONSUMER_GROUP) {
        this.MDM_MESSAGE_CONSUMER_GROUP = MDM_MESSAGE_CONSUMER_GROUP;
    }
}
