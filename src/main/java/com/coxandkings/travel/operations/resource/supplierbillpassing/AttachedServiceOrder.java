package com.coxandkings.travel.operations.resource.supplierbillpassing;

import java.math.BigDecimal;

public class AttachedServiceOrder {

    private String serviceOrderId;
    private String serviceOrderType;
    private String bookingRefNo;
    private String supplierId;
    private String productName;
    private BigDecimal serviceOrderValue;
    private BigDecimal gst;
    private BigDecimal totalCost;
    private BigDecimal netPayableToSupplier;
    private BigDecimal differenceInAmt;
    private String supplierName;
    private BigDecimal amountToBePaid;
    private String currency;

    public BigDecimal getAmountToBePaid() {
        return amountToBePaid;
    }

    public void setAmountToBePaid(BigDecimal amountToBePaid) {
        this.amountToBePaid = amountToBePaid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(String serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public String getServiceOrderType() {
        return serviceOrderType;
    }

    public void setServiceOrderType(String serviceOrderType) {
        this.serviceOrderType = serviceOrderType;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getServiceOrderValue() {
        return serviceOrderValue;
    }

    public void setServiceOrderValue(BigDecimal serviceOrderValue) {
        this.serviceOrderValue = serviceOrderValue;
    }

    public BigDecimal getGst() {
        return gst;
    }

    public void setGst(BigDecimal gst) {
        this.gst = gst;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getDifferenceInAmt() {
        return differenceInAmt;
    }

    public void setDifferenceInAmt(BigDecimal differenceInAmt) {
        this.differenceInAmt = differenceInAmt;
    }

}

