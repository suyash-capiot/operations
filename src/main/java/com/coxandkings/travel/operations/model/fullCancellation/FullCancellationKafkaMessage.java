package com.coxandkings.travel.operations.model.fullCancellation;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FULL_CANCELLATION_KAFKA_MESSAGE")
public class FullCancellationKafkaMessage {
    @EmbeddedId
    private FullCancellationIdentity fullCancellationIdentity;

    private String actionType; //as per BookingActionConstants file

    private String operation; //possible values are book, amend, etc


    private String status;

    private String errorCode;

    private String errorMessage;

    private String timestamp;


    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public FullCancellationIdentity getFullCancellationIdentity() {
        return fullCancellationIdentity;
    }

    public void setFullCancellationIdentity(FullCancellationIdentity fullCancellationIdentity) {
        this.fullCancellationIdentity = fullCancellationIdentity;
    }
}
