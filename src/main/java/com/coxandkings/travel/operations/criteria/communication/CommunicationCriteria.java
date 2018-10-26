package com.coxandkings.travel.operations.criteria.communication;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ElementCollection;
import java.time.ZonedDateTime;
import java.util.List;

public class CommunicationCriteria {

    private String communicationType;
    private String sender;
    private Boolean is_outbound ;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private ZonedDateTime dateTime ;
    private String communicationTagID;
    private String bookId ;
    private String userId ;
    private String status ;
    @ElementCollection(targetClass=String.class)
    private List<String> recipientList;
    @ElementCollection(targetClass=String.class)
    private List<String> ccRecipientList;
    @ElementCollection(targetClass=String.class)
    private List<String> bccRecipientList;
    private String supplier;
    private String productSubCategory;
    private String module;
    private String process;
    private String scenario;
    private String function ;
    private Boolean isRead;
    private Integer size;
    private Integer page;
    private String subject;

    public CommunicationCriteria() {
    }

    public String getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Boolean getIs_outbound() {
        return is_outbound;
    }

    public void setIs_outbound(Boolean is_outbound) {
        this.is_outbound = is_outbound;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCommunicationTagID() {
        return communicationTagID;
    }

    public void setCommunicationTagID(String communicationTagID) {
        this.communicationTagID = communicationTagID;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<String> recipientList) {
        this.recipientList = recipientList;
    }

    public List<String> getCcRecipientList() {
        return ccRecipientList;
    }

    public void setCcRecipientList(List<String> ccRecipientList) {
        this.ccRecipientList = ccRecipientList;
    }

    public List<String> getBccRecipientList() {
        return bccRecipientList;
    }

    public void setBccRecipientList(List<String> bccRecipientList) {
        this.bccRecipientList = bccRecipientList;
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSubject() {

        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
