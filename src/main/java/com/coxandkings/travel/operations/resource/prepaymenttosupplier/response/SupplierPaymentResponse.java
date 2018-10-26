package com.coxandkings.travel.operations.resource.prepaymenttosupplier.response;


import java.util.List;

public class SupplierPaymentResponse {

    private String id;
    private String bookingRefId;
    private String orderId;
    private String supplierRefId;
    private String supplierName;
    private String paymentDueDate;
    private String dayOfPayment;
    private String supplierCurrency;
    private String netPayableToSupplier;
    private String balanceAmtPayableToSupplier;
    private Boolean prePaymentApplicable;
    private String approverRemark;
    private String financeApprovalStatus;
    private PaymentAdviceResponse paymentAdviceResponse;
    private List<String> availableSupplierCurrencies;
    private Boolean isUiTrigger;


    public Boolean getUiTrigger() {
        return isUiTrigger;
    }

    public void setUiTrigger(Boolean uiTrigger) {
        isUiTrigger = uiTrigger;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSupplierRefId() {
        return supplierRefId;
    }

    public void setSupplierRefId(String supplierRefId) {
        this.supplierRefId = supplierRefId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getDayOfPayment() {
        return dayOfPayment;
    }

    public void setDayOfPayment(String dayOfPayment) {
        this.dayOfPayment = dayOfPayment;
    }

    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public String getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(String netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }

    public String getBalanceAmtPayableToSupplier() {
        return balanceAmtPayableToSupplier;
    }

    public void setBalanceAmtPayableToSupplier(String balanceAmtPayableToSupplier) {
        this.balanceAmtPayableToSupplier = balanceAmtPayableToSupplier;
    }

    public Boolean getPrePaymentApplicable() {
        return prePaymentApplicable;
    }

    public void setPrePaymentApplicable(Boolean prePaymentApplicable) {
        this.prePaymentApplicable = prePaymentApplicable;
    }

    public String getApproverRemark() {
        return approverRemark;
    }

    public void setApproverRemark(String approverRemark) {
        this.approverRemark = approverRemark;
    }

    public String getFinanceApprovalStatus() {
        return financeApprovalStatus;
    }

    public void setFinanceApprovalStatus(String financeApprovalStatus) {
        this.financeApprovalStatus = financeApprovalStatus;
    }

    public PaymentAdviceResponse getPaymentAdviceResponse() {
        return paymentAdviceResponse;
    }

    public void setPaymentAdviceResponse(PaymentAdviceResponse paymentAdviceResponse) {
        this.paymentAdviceResponse = paymentAdviceResponse;
    }

    public List<String> getAvailableSupplierCurrencies() {
        return availableSupplierCurrencies;
    }

    public void setAvailableSupplierCurrencies(List<String> availableSupplierCurrencies) {
        this.availableSupplierCurrencies = availableSupplierCurrencies;
    }
}




