package com.coxandkings.travel.operations.resource.failure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Comparator;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FailureDetailsResource  {

    private String failureFlag;

    private String bookID;

    private List<String> duplicateExists;

    private String bookingDate;

    private String clientName;

    private String clientID;

    private String clientType;

    private String pointOfSale;

    private String companyDetails;

    private List<ProductSummary> productSummary;

    private String paymentStatus;

    private String refundStatus;

    private String reasonForFailure;

    private Integer communicationCount;

    private String supplierId;

    private Integer numberOfPages;

    private String clientCategory;

    private String clientSubCategory;

    private boolean isRead;

    private String fileHandlerName;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory = clientCategory;
    }

    public String getClientSubCategory() {
        return clientSubCategory;
    }

    public void setClientSubCategory(String clientSubCategory) {
        this.clientSubCategory = clientSubCategory;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }


    public String getFailureFlag() {
        return failureFlag;
    }

    public void setFailureFlag(String failureFlag) {
        this.failureFlag = failureFlag;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public List<String> getDuplicateExists() {
        return duplicateExists;
    }

    public void setDuplicateExists(List<String> duplicateExists) {
        this.duplicateExists = duplicateExists;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public String getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(String companyDetails) {
        this.companyDetails = companyDetails;
    }

    public List<ProductSummary> getProductSummary() {
        return productSummary;
    }

    public void setProductSummary(List<ProductSummary> productSummary) {
        this.productSummary = productSummary;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getReasonForFailure() {
        return reasonForFailure;
    }

    public void setReasonForFailure(String reasonForFailure) {
        this.reasonForFailure = reasonForFailure;
    }

    public String getFileHandlerName() {
        return fileHandlerName;
    }

    public void setFileHandlerName(String fileHandlerName) {
        this.fileHandlerName = fileHandlerName;
    }

    public Integer getCommunicationCount() {
        return communicationCount;
    }

    public void setCommunicationCount(Integer communicationCount) {
        this.communicationCount = communicationCount;
    }
}
