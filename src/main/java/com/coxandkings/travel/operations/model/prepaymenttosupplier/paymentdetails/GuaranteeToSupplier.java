package com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "PaymentDetailsGuaranteeToSupplier")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GuaranteeToSupplier {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "modeOfPayment")
    private String modeOfPayment; // TODO: From Payment Mode Master

    @Column(name = "dateOfGuarantee")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime dateOfGuarantee;

    @Column(name = "supplierCurrency")
    private String supplierCurrency; // TODO: prepopulate as per payment Advice

    @Column(name = "guaranteeForAmtToSupplier")
    private BigDecimal guaranteeForAmtToSupplier;

    @OneToOne(cascade = CascadeType.ALL)
    private CreditCard creditCard;

    /*@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentDetailsId")
    @JsonIgnore
    private PaymentDetails paymentDetails;*/

/*

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }*/

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

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
