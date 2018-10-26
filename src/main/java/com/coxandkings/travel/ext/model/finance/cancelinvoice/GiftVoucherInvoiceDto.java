package com.coxandkings.travel.ext.model.finance.cancelinvoice;

import java.util.ArrayList;
import java.util.List;


public class GiftVoucherInvoiceDto {

    private String bookingReferenceNumber;
    private Long date;
    private String salesOffice;
    private String salesOfficer;
    private String salesState;
    private List<BatchTypeEntity> batch = new ArrayList<>();
    private Long quantity;
    private Double totalPrice;
    private Double discountPercentage;
    private Double discountPrice;
    private Double netPayablePrice;
    private Boolean discounted;
    private String invoiceNumber;
    private List<ProductCategoryEntity> productDetails = new ArrayList<>();
    private String invoiceType = "FINAL";
    private String businessUnit;
    private String currency;
    private String clientAddress;
    private String clientAccountNumber;
    private String clientType;
    private String clientId;
    private String clientName;
    private Long createdOn;

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getSalesOffice() {
        return salesOffice;
    }

    public void setSalesOffice(String salesOffice) {
        this.salesOffice = salesOffice;
    }

    public String getSalesOfficer() {
        return salesOfficer;
    }

    public void setSalesOfficer(String salesOfficer) {
        this.salesOfficer = salesOfficer;
    }

    public String getSalesState() {
        return salesState;
    }

    public void setSalesState(String salesState) {
        this.salesState = salesState;
    }

    public List<BatchTypeEntity> getBatch() {
        return batch;
    }

    public void setBatch(List<BatchTypeEntity> batch) {
        this.batch = batch;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Double getNetPayablePrice() {
        return netPayablePrice;
    }

    public void setNetPayablePrice(Double netPayablePrice) {
        this.netPayablePrice = netPayablePrice;
    }

    public Boolean getDiscounted() {
        return discounted;
    }

    public void setDiscounted(Boolean discounted) {
        this.discounted = discounted;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public List<ProductCategoryEntity> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductCategoryEntity> productDetails) {
        this.productDetails = productDetails;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientAccountNumber() {
        return clientAccountNumber;
    }

    public void setClientAccountNumber(String clientAccountNumber) {
        this.clientAccountNumber = clientAccountNumber;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }
}
