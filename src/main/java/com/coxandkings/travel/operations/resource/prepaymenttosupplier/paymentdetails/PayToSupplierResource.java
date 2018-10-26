package com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PayToSupplierResource {

    private String id;
    private String modeOfPayment;
    private String supplierCurrency;
    private String paymentReferenceNumber;
    private String sapReferenceNumber;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime dateOfPayment;

    private String remittanceCurrency;
    private BigDecimal amountRemittedToSupplier;
    private BigDecimal msfAndRemittanceCharges;
    private BigDecimal amountPayableForSupplier;
    private BigDecimal roe;
    private BigDecimal totalAmountRemitted;
    private String remarks;
    private WireTransferOrNeftResource wireTransferOrNeft;
    private ChequeOrDdDetailsResource chequeOrDdDetails;
    private CreditCardResource creditCard;

    private String paymentDetailsResource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public String getPaymentReferenceNumber() {
        return paymentReferenceNumber;
    }

    public void setPaymentReferenceNumber(String paymentReferenceNumber) {
        this.paymentReferenceNumber = paymentReferenceNumber;
    }

    public String getSapReferenceNumber() {
        return sapReferenceNumber;
    }

    public void setSapReferenceNumber(String sapReferenceNumber) {
        this.sapReferenceNumber = sapReferenceNumber;
    }

    public ZonedDateTime getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(ZonedDateTime dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public String getRemittanceCurrency() {
        return remittanceCurrency;
    }

    public void setRemittanceCurrency(String remittanceCurrency) {
        this.remittanceCurrency = remittanceCurrency;
    }

    public BigDecimal getAmountRemittedToSupplier() {
        return amountRemittedToSupplier;
    }

    public void setAmountRemittedToSupplier(BigDecimal amountRemittedToSupplier) {
        this.amountRemittedToSupplier = amountRemittedToSupplier;
    }

    public BigDecimal getMsfAndRemittanceCharges() {
        return msfAndRemittanceCharges;
    }

    public void setMsfAndRemittanceCharges(BigDecimal msfAndRemittanceCharges) {
        this.msfAndRemittanceCharges = msfAndRemittanceCharges;
    }

    public BigDecimal getAmountPayableForSupplier() {
        return amountPayableForSupplier;
    }

    public void setAmountPayableForSupplier(BigDecimal amountPayableForSupplier) {
        this.amountPayableForSupplier = amountPayableForSupplier;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    public BigDecimal getTotalAmountRemitted() {
        return totalAmountRemitted;
    }

    public void setTotalAmountRemitted(BigDecimal totalAmountRemitted) {
        this.totalAmountRemitted = totalAmountRemitted;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public WireTransferOrNeftResource getWireTransferOrNeft() {
        return wireTransferOrNeft;
    }

    public void setWireTransferOrNeft(WireTransferOrNeftResource wireTransferOrNeft) {
        this.wireTransferOrNeft = wireTransferOrNeft;
    }

    public ChequeOrDdDetailsResource getChequeOrDdDetails() {
        return chequeOrDdDetails;
    }

    public void setChequeOrDdDetails(ChequeOrDdDetailsResource chequeOrDdDetails) {
        this.chequeOrDdDetails = chequeOrDdDetails;
    }

    public CreditCardResource getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardResource creditCard) {
        this.creditCard = creditCard;
    }

    public String getPaymentDetailsResource() {
        return paymentDetailsResource;
    }

    public void setPaymentDetailsResource(String paymentDetailsResource) {
        this.paymentDetailsResource = paymentDetailsResource;
    }
}
