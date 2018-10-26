package com.coxandkings.travel.operations.resource.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationResource {

    private String _id;
    private String jobId;
    private String businessProcess;
    private String function;
    private String triggerCriteria;
    private String jobStatus;
    private String triggeredOn;
    private String completedOn;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String lastUpdated;
    private Boolean deleted;
    private Integer __v;
    private AlertNameResource alert;
    private CompanyNameResource company;
    private List<RecipientNotification> recipients;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public String getTriggerCriteria() {
        return triggerCriteria;
    }

    public void setTriggerCriteria(String triggerCriteria) {
        this.triggerCriteria = triggerCriteria;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTriggeredOn() {
        return triggeredOn;
    }

    public void setTriggeredOn(String triggeredOn) {
        this.triggeredOn = triggeredOn;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public AlertNameResource getAlert() {
        return alert;
    }

    public void setAlert(AlertNameResource alert) {
        this.alert = alert;
    }

    public CompanyNameResource getCompany() {
        return company;
    }

    public void setCompany(CompanyNameResource company) {
        this.company = company;
    }

    public List<RecipientNotification> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<RecipientNotification> recipients) {
        this.recipients = recipients;
    }
}
