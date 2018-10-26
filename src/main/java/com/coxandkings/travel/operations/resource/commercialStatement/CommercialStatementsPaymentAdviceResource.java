package com.coxandkings.travel.operations.resource.commercialStatement;

import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

public class CommercialStatementsPaymentAdviceResource {
    private String paymentAdviceNumber;
    private String supplierOrClientId;
    private String supplierOrClientName;
    private BigDecimal netPayableToSupplierOrClient;
    private BigDecimal amountPaidToSupplierOrClient;
    private BigDecimal amountToBePaid;
    private BigDecimal balanceAmtPayableToSupplierOrClient;
    private BigDecimal totalPayable;
    private  BigDecimal totalPaid;

    public BigDecimal getAmountToBePaid() {
        return amountToBePaid;
    }

    public void setAmountToBePaid(BigDecimal amountToBePaid) {
        this.amountToBePaid = amountToBePaid;
    }

    private String modeOfPayment;
    private BigDecimal paymentAdviceAmount;
    private String currency;
    @JsonProperty("paymentDueDate")
    @JsonSerialize(using = ZonedDateSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime paymentDueDate;
    private Set<AttachedCommercialStatement> attachedCommercialStatements;
    private String commercialStatementFor;
    private PaymentAdviceStatusValues paymentAdviceStatus;
    private String dayOfPaymentAdviceGeneration;
    private String approverRemark;
    private String paymentRemark;
    private BigDecimal serviceTax;

    public BigDecimal getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(BigDecimal serviceTax) {
        this.serviceTax = serviceTax;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getApproverRemark() {
        return approverRemark;
    }

    public void setApproverRemark(String approverRemark) {
        this.approverRemark = approverRemark;
    }

    public String getPaymentRemark() {
        return paymentRemark;
    }

    public void setPaymentRemark(String paymentRemark) {
        this.paymentRemark = paymentRemark;
    }

    public String getDayOfPaymentAdviceGeneration() {
        return dayOfPaymentAdviceGeneration;
    }

    public void setDayOfPaymentAdviceGeneration(String dayOfPaymentAdviceGeneration) {
        this.dayOfPaymentAdviceGeneration = dayOfPaymentAdviceGeneration;
    }

    public String getDayOfPayment() {
        return dayOfPayment;
    }

    public void setDayOfPayment(String dayOfPayment) {
        this.dayOfPayment = dayOfPayment;
    }

    private String dayOfPayment;



    public String getPaymentAdviceNumber() {
        return paymentAdviceNumber;
    }

    public void setPaymentAdviceNumber(String paymentAdviceNumber) {
        this.paymentAdviceNumber = paymentAdviceNumber;
    }

    public String getSupplierOrClientId() {
        return supplierOrClientId;
    }

    public void setSupplierOrClientId(String supplierOrClientId) {
        this.supplierOrClientId = supplierOrClientId;
    }

    public String getSupplierOrClientName() {
        return supplierOrClientName;
    }

    public void setSupplierOrClientName(String supplierOrClientName) {
        this.supplierOrClientName = supplierOrClientName;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public ZonedDateTime getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(ZonedDateTime paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public Set<AttachedCommercialStatement> getAttachedCommercialStatements() {
        return attachedCommercialStatements;
    }

    public void setAttachedCommercialStatements(Set<AttachedCommercialStatement> attachedCommercialStatements) {
        this.attachedCommercialStatements = attachedCommercialStatements;
    }

    public String getCommercialStatementFor() {
        return commercialStatementFor;
    }

    public void setCommercialStatementFor(String commercialStatementFor) {
        this.commercialStatementFor = commercialStatementFor;
    }

    public BigDecimal getAmountPaidToSupplierOrClient() {
        return amountPaidToSupplierOrClient;
    }

    public void setAmountPaidToSupplierOrClient(BigDecimal amountPaidToSupplierOrClient) {
        this.amountPaidToSupplierOrClient = amountPaidToSupplierOrClient;
    }

    public BigDecimal getBalanceAmtPayableToSupplierOrClient() {
        return balanceAmtPayableToSupplierOrClient;
    }

    public void setBalanceAmtPayableToSupplierOrClient(BigDecimal balanceAmtPayableToSupplierOrClient) {
        this.balanceAmtPayableToSupplierOrClient = balanceAmtPayableToSupplierOrClient;
    }

    public BigDecimal getPaymentAdviceAmount() {
        return paymentAdviceAmount;
    }

    public void setPaymentAdviceAmount(BigDecimal paymentAdviceAmount) {
        this.paymentAdviceAmount = paymentAdviceAmount;
    }

    public BigDecimal getNetPayableToSupplierOrClient() {
        return netPayableToSupplierOrClient;
    }

    public void setNetPayableToSupplierOrClient(BigDecimal netPayableToSupplierOrClient) {
        this.netPayableToSupplierOrClient = netPayableToSupplierOrClient;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentAdviceStatusValues getPaymentAdviceStatus() {
        return paymentAdviceStatus;
    }

    public void setPaymentAdviceStatus(PaymentAdviceStatusValues paymentAdviceStatus) {
        this.paymentAdviceStatus = paymentAdviceStatus;
    }

    public BigDecimal getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(BigDecimal totalPayable) {
        this.totalPayable = totalPayable;
    }
}
