package com.coxandkings.travel.operations.model.accountsummary;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.List;

public class BookingPaymentAdviseInfo {

    private String supplierName;

    private BigDecimal netPayableToSupplier;

    private String paymentCurrency;

    @JsonIgnore
    private String supplierID;

    private String productType;

    private List<PaymentAdvisePaymentDetails> paymentAdviseDetailsList;

    public BookingPaymentAdviseInfo()  {
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public List<PaymentAdvisePaymentDetails> getPaymentAdviseDetailsList() {
        return paymentAdviseDetailsList;
    }

    public void setPaymentAdviseDetailsList(List<PaymentAdvisePaymentDetails> paymentAdviseDetailsList) {
        this.paymentAdviseDetailsList = paymentAdviseDetailsList;
    }
}