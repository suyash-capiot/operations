package com.coxandkings.travel.operations.model.sms;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table
public class InboundSMS  {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column
    private String messageFrom;
    @Column
    private String messageTo;
    @Column
    private String type;
    @Column
    private String text;
    @Column
    private String messageUUID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessageUUID() {
        return messageUUID;
    }

    public void setMessageUUID(String messageUUID) {
        this.messageUUID = messageUUID;
    }

    @Override
    public String toString() {
        return "InboundSMS{" +
                "id='" + id + '\'' +
                ", messageFrom='" + messageFrom + '\'' +
                ", messageTo='" + messageTo + '\'' +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", messageUUID='" + messageUUID + '\'' +
                '}';
    }
}
