package com.coxandkings.travel.operations.model.communication;

import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BaseCommunication extends BaseModel {

    private String subject;

    @Column(name = "body", columnDefinition = "text", length = 10485760)
    private String body;

    private String communicationType;
    private Boolean is_outbound;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private ZonedDateTime dateTime;
    private String communicationTagID;
    private String bookId;
    private String userId;
    private String status;
    private String sender;
    @ElementCollection(targetClass = String.class)
    private List<String> recipientList;
    @ElementCollection(targetClass = String.class)
    private List<String> ccRecipientList;
    @ElementCollection(targetClass = String.class)
    private List<String> bccRecipientList;
    private String fromMail ;

//    @OneToOne(mappedBy = "baseCommunication", cascade = CascadeType.ALL)
//    private List<Recipient> recipientList;
//    @OneToOne(mappedBy = "baseCommunication", cascade = CascadeType.ALL)
//    private List<CCRecipient> ccRecipientList;
//    @OneToOne(mappedBy = "baseCommunication", cascade = CascadeType.ALL)
//    private List<BCCRecipient> bccRecipientList;


    private String supplier;
    private String productSubCategory;
    private String module;
    private String process;
    private String scenario;
    private String function;
    //@Column(columnDefinition = "false")
    private Boolean isRead;
    @OneToOne(mappedBy = "baseCommunication", cascade = CascadeType.ALL)
    private CommunicationTags communicationTags;

    @PrePersist
    public void onPrePersist() {
        setDateTime(ZonedDateTime.now(ZoneId.systemDefault()));
        setRead(false);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }

    public Boolean getIs_outbound() {
        return is_outbound;
    }

    public void setIs_outbound(Boolean is_outbound) {
        this.is_outbound = is_outbound;
    }

    public ZonedDateTime getDateTime() {
        return dateTime.withFixedOffsetZone();
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

//    public List<Recipient> getRecipientList() {
//        return recipientList;
//    }
//
//    public void setRecipientList(List<Recipient> recipientList) {
//        this.recipientList = recipientList;
//    }
//
//    public List<CCRecipient> getCcRecipientList() {
//        return ccRecipientList;
//    }
//
//    public void setCcRecipientList(List<CCRecipient> ccRecipientList) {
//        this.ccRecipientList = ccRecipientList;
//    }
//
//    public List<BCCRecipient> getBccRecipientList() {
//        return bccRecipientList;
//    }
//
//    public void setBccRecipientList(List<BCCRecipient> bccRecipientList) {
//        this.bccRecipientList = bccRecipientList;
//    }


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

    public CommunicationTags getCommunicationTags() {
        return communicationTags;
    }

    public void setCommunicationTags(CommunicationTags communicationTags) {
        this.communicationTags = communicationTags;
    }

    public Boolean getRead() {
        isRead = (isRead == null) ? false : isRead;
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }
}
