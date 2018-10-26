package com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.commercialstatements.PaymentAdviceStatementInfo;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdviceOrderInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class SupplierPaymentResource {

    private String id;
    private Set<PaymentAdviceOrderInfo> paymentAdviceOrderInfoSet;
    private Set<PaymentAdviceStatementInfo> paymentAdviceStatementInfoSet;

    private String supplierRefId;
    private String supplierName;

    @JsonProperty("paymentDueDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime paymentDueDate;

    private String dayOfPayment;
    private String supplierCurrency;
    private BigDecimal netPayableToSupplier;
    private BigDecimal balanceAmtPayableToSupplier;
    private Boolean prePaymentApplicable;
    private String approverRemark;
    private String financeApprovalStatus;
    private PaymentAdviceResource paymentAdviceResource;
    private List<String> availableSupplierCurrencies;
    private String documentReferenceId;

    public String getDocumentReferenceId() {
        return documentReferenceId;
    }

    public void setDocumentReferenceId(String documentReferenceId) {
        this.documentReferenceId = documentReferenceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ZonedDateTime getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(ZonedDateTime paymentDueDate) {
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

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }

    public BigDecimal getBalanceAmtPayableToSupplier() {
        return balanceAmtPayableToSupplier;
    }

    public void setBalanceAmtPayableToSupplier(BigDecimal balanceAmtPayableToSupplier) {
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

    public PaymentAdviceResource getPaymentAdviceResource() {
        return paymentAdviceResource;
    }

    public void setPaymentAdviceResource(PaymentAdviceResource paymentAdviceResource) {
        this.paymentAdviceResource = paymentAdviceResource;
    }

    public List<String> getAvailableSupplierCurrencies() {
        return availableSupplierCurrencies;
    }

    public void setAvailableSupplierCurrencies(List<String> availableSupplierCurrencies) {
        this.availableSupplierCurrencies = availableSupplierCurrencies;
    }

    public Set<PaymentAdviceOrderInfo> getPaymentAdviceOrderInfoSet() {
        return paymentAdviceOrderInfoSet;
    }

    public void setPaymentAdviceOrderInfoSet(Set<PaymentAdviceOrderInfo> paymentAdviceOrderInfoSet) {
        this.paymentAdviceOrderInfoSet = paymentAdviceOrderInfoSet;
    }

    public Set<PaymentAdviceStatementInfo> getPaymentAdviceStatementInfoSet() {
        return paymentAdviceStatementInfoSet;
    }

    public void setPaymentAdviceStatementInfoSet(Set<PaymentAdviceStatementInfo> paymentAdviceStatementInfoSet) {
        this.paymentAdviceStatementInfoSet = paymentAdviceStatementInfoSet;
    }
}
