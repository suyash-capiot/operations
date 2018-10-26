package com.coxandkings.travel.operations.resource.callrecord;

import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;

import java.util.List;

public class CallRecordResource {

    private String id;
    private String callFrom;
    private String callTo;
    private String phoneNumber;
    private String direction;
    private String duration;
    private String description;
    private String subject;
    private List<String> mediaId;
    private String bookId;
    private String userId;
    private String supplier;
    private String productSubCategory;
    private String process;
    private String scenario;
    private String function ;
    private CommunicationTagResource communicationTagResource;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCallFrom() {
        return callFrom;
    }

    public void setCallFrom(String callFrom) {
        this.callFrom = callFrom;
    }

    public String getCallTo() {
        return callTo;
    }

    public void setCallTo(String callTo) {
        this.callTo = callTo;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getMediaId() {
        return mediaId;
    }

    public void setMediaId(List<String> mediaId) {
        this.mediaId = mediaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public CommunicationTagResource getCommunicationTagResource() {
        return communicationTagResource;
    }

    public void setCommunicationTagResource(CommunicationTagResource communicationTagResource) {
        this.communicationTagResource = communicationTagResource;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
