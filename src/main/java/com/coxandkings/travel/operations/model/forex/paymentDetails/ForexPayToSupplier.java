package com.coxandkings.travel.operations.model.forex.paymentDetails;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ForexPayToSupplier {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "modeOfPayment")
    private String modeOfPayment; // TODO: From Payment Mode Master

    @Column(name = "paymentReferenceNumber")
    private String paymentReferenceNumber; //TODO: entered by Finanace User

    @Column(name = "sapReferenceNumber")
    private String sapReferenceNumber;

    @Column(name = "dateOfPayment")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime dateOfPayment;//TODO: As per payment date

    @Column(name = "rate_of_exchange")
    private BigDecimal roe;                         //TODO: RE Master

    @Column(name = "totalAmountRemitted")
    private BigDecimal totalAmountRemitted;   // TODO: amountRemittedToSupplier + msfAndRemittanceCharges

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "paymentAdviceNo")
    private String paymentAdviceNo;   //TODO : May have to link with finance

    @OneToOne(cascade = CascadeType.ALL)
    private ForexWireTransferOrNeft forexWireTransferOrNeft;

    @OneToOne(cascade = CascadeType.ALL)
    private ForexChequeOrDdDetails chequeOrDdDetails;

    @OneToOne(cascade = CascadeType.ALL)
    private ForexCreditCard forexCreditCard;

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


    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    public String getPaymentAdviceNo() {
        return paymentAdviceNo;
    }

    public void setPaymentAdviceNo(String paymentAdviceNo) {
        this.paymentAdviceNo = paymentAdviceNo;
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

    public ForexWireTransferOrNeft getForexWireTransferOrNeft() {
        return forexWireTransferOrNeft;
    }

    public void setForexWireTransferOrNeft(ForexWireTransferOrNeft forexWireTransferOrNeft) {
        this.forexWireTransferOrNeft = forexWireTransferOrNeft;
    }

    public ForexChequeOrDdDetails getChequeOrDdDetails() {
        return chequeOrDdDetails;
    }

    public void setChequeOrDdDetails(ForexChequeOrDdDetails chequeOrDdDetails) {
        this.chequeOrDdDetails = chequeOrDdDetails;
    }

    public ForexCreditCard getForexCreditCard() {
        return forexCreditCard;
    }

    public void setForexCreditCard(ForexCreditCard forexCreditCard) {
        this.forexCreditCard = forexCreditCard;
    }


}
