package com.coxandkings.travel.operations.resource.email;

import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;

import java.util.List;

public class EmailUsingBodyAndDocumentsResource {

    private String fromMail ;
    private List<String> toMail ;
    private List<String> ccMail ;
    private List<String> bccMail ;
    private String subject ;
    private EmailPriority priority ;
    private String body ;
    private List<String> documentReferenceIDs;
    private List<FileAttachmentResource> fileAttachments;
    private CommunicationTagResource communicationTagResource;
    private String bookId;
    private String userId;
    private String supplier;
    private String productSubCategory;
    private String process;
    private String scenario;
    private String function ;

    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    public List<String> getToMail() {
        return toMail;
    }

    public void setToMail(List<String> toMail) {
        this.toMail = toMail;
    }

    public List<String> getCcMail() {
        return ccMail;
    }

    public void setCcMail(List<String> ccMail) {
        this.ccMail = ccMail;
    }

    public List<String> getBccMail() {
        return bccMail;
    }

    public void setBccMail(List<String> bccMail) {
        this.bccMail = bccMail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public EmailPriority getPriority() {
        return priority;
    }

    public void setPriority(EmailPriority priority) {
        this.priority = priority;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getDocumentReferenceIDs() {
        return documentReferenceIDs;
    }

    public void setDocumentReferenceIDs(List<String> documentReferenceIDs) {
        this.documentReferenceIDs = documentReferenceIDs;
    }

    public List<FileAttachmentResource> getFileAttachments() {
        return fileAttachments;
    }

    public void setFileAttachments(List<FileAttachmentResource> fileAttachments) {
        this.fileAttachments = fileAttachments;
    }

    public CommunicationTagResource getCommunicationTagResource() {
        return communicationTagResource;
    }

    public void setCommunicationTagResource(CommunicationTagResource communicationTagResource) {
        this.communicationTagResource = communicationTagResource;
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
}
