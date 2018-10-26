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
@Table(name = "PaymentDetailsPayToSupplier")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PayToSupplier {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "modeOfPayment")
    private String modeOfPayment; // TODO: From Payment Mode Master

    @Column(name = "supplierCurrency")
    private String supplierCurrency; // TODO: prepopulate as per payment Advice

    @Column(name = "paymentReferenceNumber")
    private String paymentReferenceNumber; //TODO: entered by Finanace User

    @Column(name = "sapReferenceNumber")
    private String sapReferenceNumber;

    @Column(name = "dateOfPayment")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime dateOfPayment;//TODO: As per payment date

    @Column(name = "remittanceCurrency")
    private String remittanceCurrency; //TODO: by default, Supplier Currency

    @Column(name = "amountRemittedToSupplier")
    private BigDecimal amountRemittedToSupplier; // Converted Value

    @Column(name = "msfAndRemittanceCharges")
    private BigDecimal msfAndRemittanceCharges;

    @Column(name = "amountPayableForSupplier")
    private BigDecimal amountPayableForSupplier; //TODO: prepopulated as per paymentAdviceNumber

    @Column(name = "rate_of_exchange")
    private BigDecimal roe;                         //TODO: RE Master

    @Column(name = "totalAmountRemitted")
    private BigDecimal totalAmountRemitted;   // TODO: amountRemittedToSupplier + msfAndRemittanceCharges

    @Column(name = "remarks")
    private String remarks;

    @OneToOne(cascade = CascadeType.ALL)
    private WireTransferOrNeft wireTransferOrNeft;

    @OneToOne(cascade = CascadeType.ALL)
    private ChequeOrDdDetails chequeOrDdDetails;

    @OneToOne(cascade = CascadeType.ALL)
    private CreditCard creditCard;






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

    public BigDecimal getAmountPayableForSupplier() {
        return amountPayableForSupplier;
    }

    public void setAmountPayableForSupplier(BigDecimal amountPayableForSupplier) {
        this.amountPayableForSupplier = amountPayableForSupplier;
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

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
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

    public WireTransferOrNeft getWireTransferOrNeft() {
        return wireTransferOrNeft;
    }

    public void setWireTransferOrNeft(WireTransferOrNeft wireTransferOrNeft) {
        this.wireTransferOrNeft = wireTransferOrNeft;
    }

    public ChequeOrDdDetails getChequeOrDdDetails() {
        return chequeOrDdDetails;
    }

    public void setChequeOrDdDetails(ChequeOrDdDetails chequeOrDdDetails) {
        this.chequeOrDdDetails = chequeOrDdDetails;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }


}
