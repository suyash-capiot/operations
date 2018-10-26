package com.coxandkings.travel.operations.resource.notification;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.concurrent.ConcurrentHashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InlineMessageResource {

    private String alertName;
    private String notificationType;
    private ConcurrentHashMap<String, String> dynamicVariables = new ConcurrentHashMap<>();


    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public ConcurrentHashMap<String, String> getDynamicVariables() {
        return dynamicVariables;
    }

    public void setDynamicVariables(ConcurrentHashMap<String, String> dynamicVariables) {
        this.dynamicVariables = dynamicVariables;
    }
}
