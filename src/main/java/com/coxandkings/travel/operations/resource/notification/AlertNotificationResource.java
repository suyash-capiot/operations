package com.coxandkings.travel.operations.resource.notification;

import java.util.List;

public class AlertNotificationResource{
    private String __id;
    private String company;
    private String alertType;
    private String alertName;
    private String businessProcess;
    private String function;
    private String status;
    private String effectiveFrom;
    private String createdBy;
    private Integer __v;
    private Boolean deleted;
    private String lastUpdated;
    private String createdAt;
    private RecipientAlert recipients;
    private List<NotificationAlert> notification;
    private ScheduleResource schedule;
    private List<ConditionsResource> conditions;
    private List<OuputParamsResource> outputParams;
    private List<InputParamsResource> inputParams;


    public String get__id() {
        return __id;
    }

    public void set__id(String __id) {
        this.__id = __id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public RecipientAlert getRecipients() {
        return recipients;
    }

    public void setRecipients(RecipientAlert recipients) {
        this.recipients = recipients;
    }

    public List<NotificationAlert> getNotification() {
        return notification;
    }

    public void setNotification(List<NotificationAlert> notification) {
        this.notification = notification;
    }

    public ScheduleResource getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleResource schedule) {
        this.schedule = schedule;
    }

    public List<ConditionsResource> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionsResource> conditions) {
        this.conditions = conditions;
    }

    public List<OuputParamsResource> getOutputParams() {
        return outputParams;
    }

    public void setOutputParams(List<OuputParamsResource> outputParams) {
        this.outputParams = outputParams;
    }

    public List<InputParamsResource> getInputParams() {
        return inputParams;
    }

    public void setInputParams(List<InputParamsResource> inputParams) {
        this.inputParams = inputParams;
    }
}
