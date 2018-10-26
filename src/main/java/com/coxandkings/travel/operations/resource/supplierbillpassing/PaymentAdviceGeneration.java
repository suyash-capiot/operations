package com.coxandkings.travel.operations.resource.supplierbillpassing;

import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

public class PaymentAdviceGeneration {
    private String paymentAdviceNumber;
    private String supplierRefId;
    private String supplierName;
    private BigDecimal netPayableToSupplier;
    private BigDecimal amountPaidToSupplier;
    private BigDecimal balanceAmtPayableToSupplier;
    private String modeOfPayment;
    private String currency;
    private BigDecimal amountToBePaid;
    @JsonProperty("paymentDueDate")
    @JsonSerialize(using = ZonedDateSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime paymentDueDate;
    private Set<AttachedServiceOrder> attachedServiceOrderSet;
    private PaymentAdviceStatusValues paymentAdviceStatus;

    public String getPaymentAdviceNumber() {
        return paymentAdviceNumber;
    }

    public void setPaymentAdviceNumber(String paymentAdviceNumber) {
        this.paymentAdviceNumber = paymentAdviceNumber;
    }

    public String getSupplierRefId() {
        return supplierRefId;
    }

    public void setSupplierRefId(String supplierRefId) {
        this.supplierRefId = supplierRefId;
    }

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }

    public BigDecimal getAmountPaidToSupplier() {
        return amountPaidToSupplier;
    }

    public void setAmountPaidToSupplier(BigDecimal amountPaidToSupplier) {
        this.amountPaidToSupplier = amountPaidToSupplier;
    }

    public BigDecimal getBalanceAmtPayableToSupplier() {
        return balanceAmtPayableToSupplier;
    }

    public void setBalanceAmtPayableToSupplier(BigDecimal balanceAmtPayableToSupplier) {
        this.balanceAmtPayableToSupplier = balanceAmtPayableToSupplier;
    }

    public BigDecimal getAmountToBePaid() {
        return amountToBePaid;
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

    public void setAmountToBePaid(BigDecimal amountToBePaid) {
        this.amountToBePaid = amountToBePaid;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Set<AttachedServiceOrder> getAttachedServiceOrderSet() {
        return attachedServiceOrderSet;
    }

    public void setAttachedServiceOrderSet(Set<AttachedServiceOrder> attachedServiceOrderSet) {
        this.attachedServiceOrderSet = attachedServiceOrderSet;
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
}
