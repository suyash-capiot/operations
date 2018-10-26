package com.coxandkings.travel.operations.model.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class KafkaBookingMessage implements Serializable {

    @JsonProperty("BookID")
    private String bookId;

    @JsonProperty("type")
    private String actionType; //as per BookingActionConstants file

    @JsonProperty("Operation")
    private String operation; //possible values are book, amend, etc

    @JsonProperty("orderID")
    private String orderNo;

    @JsonProperty("status")
    private String status;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("timestamp")
    private String timestamp;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    @Override
    public String toString() {
        return "KafkaBookingMessage{" +
                "bookId='" + bookId + '\'' +
                ", actionType='" + actionType + '\'' +
                ", operation='" + operation + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", status='" + status + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
