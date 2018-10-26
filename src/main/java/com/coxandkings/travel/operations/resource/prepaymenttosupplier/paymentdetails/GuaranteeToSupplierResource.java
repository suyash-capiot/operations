package com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class GuaranteeToSupplierResource {


    private String id;
    private String modeOfPayment;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime dateOfGuarantee;
    private String supplierCurrency;
    private BigDecimal guaranteeForAmtToSupplier;
    private CreditCardResource creditCardResource;
    private String paymentDetails;
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

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public ZonedDateTime getDateOfGuarantee() {
        return dateOfGuarantee;
    }

    public void setDateOfGuarantee(ZonedDateTime dateOfGuarantee) {
        this.dateOfGuarantee = dateOfGuarantee;
    }

    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public BigDecimal getGuaranteeForAmtToSupplier() {
        return guaranteeForAmtToSupplier;
    }

    public void setGuaranteeForAmtToSupplier(BigDecimal guaranteeForAmtToSupplier) {
        this.guaranteeForAmtToSupplier = guaranteeForAmtToSupplier;
    }

    public CreditCardResource getCreditCardResource() {
        return creditCardResource;
    }

    public void setCreditCardResource(CreditCardResource creditCardResource) {
        this.creditCardResource = creditCardResource;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
