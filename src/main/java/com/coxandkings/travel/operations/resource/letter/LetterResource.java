package com.coxandkings.travel.operations.resource.letter;

import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;

import java.util.List;

public class LetterResource {

    private String sender;
    private String receiver;
    private String address;
    private String subject;
    private String description;
    private String direction;
    private String bookId;
    private String userId;
    private String supplier;
    private String productSubCategory;
    private String process;
    private String scenario;
    private String function ;
    private CommunicationTagResource communicationTagResource;
    private List<String> documentRefIDs;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    public List<String> getDocumentRefIDs() {
        return documentRefIDs;
    }

    public void setDocumentRefIDs(List<String> documentRefIDs) {
        this.documentRefIDs = documentRefIDs;
    }
}
