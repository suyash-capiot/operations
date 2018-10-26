package com.coxandkings.travel.operations.resource.activitylog;

import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;

import java.util.List;

public class ActivityLogResource {

    private String id;
    private String userId;
    private String bookId;
    private String title;
    private String description;
    private String dueDate;
    private String concernedPerson;
    private List<String> documentRefIDs;
    private String supplier;
    private String productSubCategory;
    private String process;
    private String scenario;
    private String function ;
    private CommunicationTagResource communicationTagResource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getConcernedPerson() {
        return concernedPerson;
    }

    public void setConcernedPerson(String concernedPerson) {
        this.concernedPerson = concernedPerson;
    }

    public List<String> getDocumentRefIDs() {
        return documentRefIDs;
    }

    public void setDocumentRefIDs(List<String> documentRefIDs) {
        this.documentRefIDs = documentRefIDs;
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
