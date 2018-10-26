package com.coxandkings.travel.operations.helper.booking;

import com.coxandkings.travel.operations.helper.booking.payment.ClientPaymentDetails;
import com.coxandkings.travel.operations.helper.booking.payment.CompanyDetails;
import com.coxandkings.travel.operations.helper.booking.product.Product;
import com.coxandkings.travel.operations.helper.booking.product.Status;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class BookingDetails {
    private String bkgRefNum;
    private Map< String, String > attributes ;
    private Set<Product> products;
    private String clientId;
    private String clientTypeId;
    private ClientPaymentDetails clientPaymentDetails;
    private Status status;
    private BigDecimal totalInvoice;
    private BigDecimal totalCollection;
    private BigDecimal totalOutStandingAmount;
    private String createdByUserId;
    private Long createdTime;
    private String lastModifiedByUserId;
    private Long lastModifiedTime;

    //added for product summary
    private Long bookingDate;
    private String travelRequestId;
    private String sapFileReferenceNumber;
    private String invoiceId;
    //fields are added for wireframes.. later this will be moves as enum in bookingStatus.
    private String qcStatus;


    // added newly by shrikant
    private String bookingType;
    private Boolean assignment;
    private String financialControlId;
    private String priority;
    private String username;
    private String pointOfSale;
    private String assignTo;
    private Long travelDate;
    private CompanyDetails companyDetails;

    public String getBkgRefNum() {
        return bkgRefNum;
    }

    public void setBkgRefNum(String bkgRefNum) {
        this.bkgRefNum = bkgRefNum;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ClientPaymentDetails getClientPaymentDetails() {
        return clientPaymentDetails;
    }

    public void setClientPaymentDetails(ClientPaymentDetails clientPaymentDetails) { this.clientPaymentDetails = clientPaymentDetails; }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getTotalInvoice() {
        return totalInvoice;
    }

    public void setTotalInvoice(BigDecimal totalInvoice) {
        this.totalInvoice = totalInvoice;
    }

    public BigDecimal getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(BigDecimal totalCollection) {
        this.totalCollection = totalCollection;
    }

    public BigDecimal getTotalOutStandingAmount() {
        return totalOutStandingAmount;
    }

    public void setTotalOutStandingAmount(BigDecimal totalOutStandingAmount) { this.totalOutStandingAmount = totalOutStandingAmount; }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) { this.lastModifiedByUserId = lastModifiedByUserId; }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Long getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Long bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getTravelRequestId() {
        return travelRequestId;
    }

    public void setTravelRequestId(String travelRequestId) {
        this.travelRequestId = travelRequestId;
    }

    public String getSapFileReferenceNumber() {
        return sapFileReferenceNumber;
    }

    public void setSapFileReferenceNumber(String sapFileReferenceNumber) { this.sapFileReferenceNumber = sapFileReferenceNumber; }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public Boolean getAssignment() {
        return assignment;
    }

    public void setAssignment(Boolean assignment) {
        this.assignment = assignment;
    }

    public String getFinancialControlId() {
        return financialControlId;
    }

    public void setFinancialControlId(String financialControlId) {
        this.financialControlId = financialControlId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public Long getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Long travelDate) {
        this.travelDate = travelDate;
    }

    public CompanyDetails getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(CompanyDetails companyDetails) {
        this.companyDetails = companyDetails;
    }

    public String getClientTypeId() { return clientTypeId; }

    public void setClientTypeId(String clientTypeId) { this.clientTypeId = clientTypeId; }
}
