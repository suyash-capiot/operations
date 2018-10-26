package com.coxandkings.travel.operations.resource.chat;

import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;

public class ChatResource {

    private String transactionId;
    private String transactionType;
    private String sender;
    private String receiver;
    private String chatScript;
    private String bookId;
    private String userId;
    private String supplier;
    private String productSubCategory;
    private String process;
    private String scenario;
    private String function ;
    private CommunicationTagResource communicationTagResource;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

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

    public String getChatScript() {
        return chatScript;
    }

    public void setChatScript(String chatScript) {
        this.chatScript = chatScript;
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
}
