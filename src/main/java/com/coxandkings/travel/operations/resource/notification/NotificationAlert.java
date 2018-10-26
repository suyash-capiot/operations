package com.coxandkings.travel.operations.resource.notification;

public class NotificationAlert {

    private String __id;
    private Boolean status;
    private String notificationType;
    private String messageType;
    private String inlineText;
    private String severity;
    private Boolean isAckRequired;



    public String get__id() {
        return __id;
    }

    public void set__id(String __id) {
        this.__id = __id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getInlineText() {
        return inlineText;
    }

    public void setInlineText(String inlineText) {
        this.inlineText = inlineText;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Boolean getAckRequired() {
        return isAckRequired;
    }

    public void setAckRequired(Boolean ackRequired) {
        isAckRequired = ackRequired;
    }
}
