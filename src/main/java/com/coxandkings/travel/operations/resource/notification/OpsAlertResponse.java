package com.coxandkings.travel.operations.resource.notification;

public class OpsAlertResponse {

    private String notification;
    private String businessProcess;
    private String function;
    private String notificationOn;
    private String acknowledgedOn;
    private String action;
    private String notificationMessage;
    private String recurrenceCount;
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getBusinessProcess() {
        return businessProcess;
    }

    public void setBusinessProcess(String businessProcess) {
        this.businessProcess = businessProcess;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getNotificationOn() {
        return notificationOn;
    }

    public void setNotificationOn(String notificationOn) {
        this.notificationOn = notificationOn;
    }

    public String getAcknowledgedOn() {
        return acknowledgedOn;
    }

    public void setAcknowledgedOn(String acknowledgedOn) {
        this.acknowledgedOn = acknowledgedOn;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getRecurrenceCount() {
        return recurrenceCount;
    }

    public void setRecurrenceCount(String recurrenceCount) {
        this.recurrenceCount = recurrenceCount;
    }
}
