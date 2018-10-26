package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsPaymentInfo implements Serializable {

    @JsonProperty("cardExpiry")
    private String cardExpiry;

    @JsonProperty("paymentID")
    private String paymentID;

    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("cardType")
    private String cardType;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("encryptionKey")
    private String encryptionKey;

    @JsonProperty("paymentAmount")
    private String paymentAmount;

    @JsonProperty("cardNumber")
    private String cardNumber;

    @JsonProperty("paymentType")
    private String paymentType;

    @JsonProperty("amountCurrency")
    private String amountCurrency;

    @JsonProperty("token")
    private String token;

//  Note: Not provided by booking
    @JsonProperty("paymentStatus")
    private String payStatus;

    @JsonProperty( "totalAmount" )
    private String totalAmount;

    @JsonProperty( "amountPaid" )
    private String amountPaid;

    @JsonProperty("partPaymentSchedule")
    private List<OpsPartPaymentSchedule> partPaymentSchedule;

    @JsonProperty( "transactionRefNumber" )
    private String transactionRefNumber;

    @JsonProperty( "transactionDate" )
    private String transactionDate;

    public OpsPaymentInfo() {}

    public String getTransactionRefNumber() {
        return transactionRefNumber;
    }

    public void setTransactionRefNumber(String transactionRefNumber) {
        this.transactionRefNumber = transactionRefNumber;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(String amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public List<OpsPartPaymentSchedule> getPartPaymentSchedule() {
        return partPaymentSchedule;
    }

    public void setPartPaymentSchedule(List<OpsPartPaymentSchedule> partPaymentSchedule) {
        this.partPaymentSchedule = partPaymentSchedule;
    }

    @Override
    public String toString() {
        return "OpsPaymentInfo{" +
                "cardExpiry='" + cardExpiry + '\'' +
                ", paymentID='" + paymentID + '\'' +
                ", accountType='" + accountType + '\'' +
                ", cardType='" + cardType + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", encryptionKey='" + encryptionKey + '\'' +
                ", paymentAmount='" + paymentAmount + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", amountCurrency='" + amountCurrency + '\'' +
                ", token='" + token + '\'' +
                ", payStatus='" + payStatus + '\'' +
                '}';
    }
}
