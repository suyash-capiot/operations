package com.coxandkings.travel.operations.model.sms;

import com.coxandkings.travel.operations.model.communication.BaseCommunication;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table
public class OutboundSMS extends BaseCommunication{


    @ElementCollection
    private List<String> messageUUID;

    public List<String> getMessageUUID() {
        return messageUUID;
    }

    public void setMessageUUID(List<String> messageUUID) {
        this.messageUUID = messageUUID;
    }
}
