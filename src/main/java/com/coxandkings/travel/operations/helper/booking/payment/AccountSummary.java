package com.coxandkings.travel.operations.helper.booking.payment;


import com.coxandkings.travel.operations.model.accountsummary.BookingPaymentAdviseInfo;
import com.coxandkings.travel.operations.model.accountsummary.InvoiceBasicInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class AccountSummary {

    @JsonProperty( "ClientPaymentDetails" )
    private List<InvoiceBasicInfo> invoiceInfoList;

    @JsonProperty( "SupplierPaymentDetails" )
    private List<BookingPaymentAdviseInfo> paymentAdviseList;

    private String clientTotalCollection;

    private String clientTotalOutstandingAmount;

    private BigDecimal clientCollection;

    private BigDecimal clientOutstandingAmount;

    private String supplierTotalAmountPaid;

    private String supplierBalanceAmountDue;

    public AccountSummary() {}

    public String getClientTotalCollection() {
        return clientTotalCollection;
    }

    public void setClientTotalCollection(String clientTotalCollection) {
        this.clientTotalCollection = clientTotalCollection;
    }

    public String getClientTotalOutstandingAmount() {
        return clientTotalOutstandingAmount;
    }

    public void setClientTotalOutstandingAmount(String clientTotalOutstandingAmount) {
        this.clientTotalOutstandingAmount = clientTotalOutstandingAmount;
    }

    public String getSupplierTotalAmountPaid() {
        return supplierTotalAmountPaid;
    }

    public void setSupplierTotalAmountPaid(String supplierTotalAmountPaid) {
        this.supplierTotalAmountPaid = supplierTotalAmountPaid;
    }

    public String getSupplierBalanceAmountDue() {
        return supplierBalanceAmountDue;
    }

    public void setSupplierBalanceAmountDue(String supplierBalanceAmountDue) {
        this.supplierBalanceAmountDue = supplierBalanceAmountDue;
    }

    public List<InvoiceBasicInfo> getInvoiceInfoList() {
        return invoiceInfoList;
    }

    public void setInvoiceInfoList(List<InvoiceBasicInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    public List<BookingPaymentAdviseInfo> getPaymentAdviseList() {
        return paymentAdviseList;
    }

    public void setPaymentAdviseList(List<BookingPaymentAdviseInfo> paymentAdviseList) {
        this.paymentAdviseList = paymentAdviseList;
    }

    public BigDecimal getClientCollection() {
        return clientCollection;
    }

    public void setClientCollection(BigDecimal clientCollection) {
        this.clientCollection = clientCollection;
    }

    public BigDecimal getClientOutstandingAmount() {
        return clientOutstandingAmount;
    }

    public void setClientOutstandingAmount(BigDecimal clientOutstandingAmount) {
        this.clientOutstandingAmount = clientOutstandingAmount;
    }
}