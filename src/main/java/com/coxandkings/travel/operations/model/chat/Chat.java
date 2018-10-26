package com.coxandkings.travel.operations.model.chat;

import com.coxandkings.travel.operations.model.communication.BaseCommunication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "chat")
public class Chat extends BaseCommunication {

    private String transactionId;
    private String transactionType;
    @Column(columnDefinition = "text")
    private String chatScript;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getChatScript() {
        return chatScript;
    }

    public void setChatScript(String chatScript) {
        this.chatScript = chatScript;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "transactionId='" + transactionId + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", chatScript='" + chatScript + '\'' +
                '}';
    }
}
