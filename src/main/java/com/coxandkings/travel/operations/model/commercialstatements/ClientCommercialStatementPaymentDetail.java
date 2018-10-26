package com.coxandkings.travel.operations.model.commercialstatements;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.ChequeOrDdDetails;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.CreditCard;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.WireTransferOrNeft;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table
public class ClientCommercialStatementPaymentDetail  {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    private String clientCommercialStatementId;
    private BigDecimal amountToBePaid;

    @Column
    private String paymentType;
    @Column
    private String paymentAdviceNumber;
    @Column
    private String currency;
    @Column
    private BigDecimal amountPayable;
    @Column
    private String remittanceCurrency;
    @Column
    private String roe;
    @Column
    private BigDecimal amountToBeRemitted;
    @Column
    private BigDecimal totalAmountToBeRemitted;
    @Column
    private BigDecimal balanceAmount;
    @OneToOne(cascade = CascadeType.ALL)
    private WireTransferOrNeft wireTransferOrNeft;

    @OneToOne(cascade = CascadeType.ALL)
    private ChequeOrDdDetails chequeOrDdDetails;

    @OneToOne(cascade = CascadeType.ALL)
    private CreditCard creditCard;

    @Column(name = "paymentReferenceNumber")
    private String paymentReferenceNumber; //TODO: entered by Finanace User

    @Column(name = "sapReferenceNumber")
    private String sapReferenceNumber;

    @Column(name = "dateOfPayment")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime dateOfPayment;//TODO: As per payment date

    @Column
    private String remarks;


//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    private ZonedDateTime dateOfPayment;

    public String getClientCommercialStatementId() {
        return clientCommercialStatementId;
    }

    public void setClientCommercialStatementId(String clientCommercialStatementId) {
        this.clientCommercialStatementId = clientCommercialStatementId;
    }

    public BigDecimal getAmountToBePaid() {
        return amountToBePaid;
    }

    public void setAmountToBePaid(BigDecimal amountToBePaid) {
        this.amountToBePaid = amountToBePaid;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    //    public ZonedDateTime getDateOfPayment() {
//        return dateOfPayment;
//    }
//
//    public void setDateOfPayment(ZonedDateTime dateOfPayment) {
//        this.dateOfPayment = dateOfPayment;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentAdviceNumber() {
        return paymentAdviceNumber;
    }

    public void setPaymentAdviceNumber(String paymentAdviceNumber) {
        this.paymentAdviceNumber = paymentAdviceNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(BigDecimal amountPayable) {
        this.amountPayable = amountPayable;
    }



    public String getRemittanceCurrency() {
        return remittanceCurrency;
    }

    public void setRemittanceCurrency(String remittanceCurrency) {
        this.remittanceCurrency = remittanceCurrency;
    }

    public String getRoe() {
        return roe;
    }

    public void setRoe(String roe) {
        this.roe = roe;
    }

    public BigDecimal getAmountToBeRemitted() {
        return amountToBeRemitted;
    }

    public void setAmountToBeRemitted(BigDecimal amountToBeRemitted) {
        this.amountToBeRemitted = amountToBeRemitted;
    }

    public BigDecimal getTotalAmountToBeRemitted() {
        return totalAmountToBeRemitted;
    }

    public void setTotalAmountToBeRemitted(BigDecimal totalAmountToBeRemitted) {
        this.totalAmountToBeRemitted = totalAmountToBeRemitted;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
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
