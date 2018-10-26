package com.coxandkings.travel.operations.resource.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipientNotification {

    private String _id;
    private String user;
    private String message;
    private String alertType;
    private String status;
    private String deliveryFailureMessage;
    private Boolean acknowledged;
    private String acknowledgedOn;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryFailureMessage() {
        return deliveryFailureMessage;
    }

    public void setDeliveryFailureMessage(String deliveryFailureMessage) {
        this.deliveryFailureMessage = deliveryFailureMessage;
    }

    public Boolean getAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(Boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public String getAcknowledgedOn() {
        return acknowledgedOn;
    }

    public void setAcknowledgedOn(String acknowledgedOn) {
        this.acknowledgedOn = acknowledgedOn;
    }
}
