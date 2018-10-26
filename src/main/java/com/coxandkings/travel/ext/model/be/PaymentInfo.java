
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cardExpiry",
    "paymentID",
    "accountType",
    "cardType",
    "paymentMethod",
    "encryptionKey",
    "paymentAmount",
    "cardNumber",
    "paymentType",
    "amountCurrency",
    "token"
})
public class PaymentInfo implements Serializable
{

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
    @JsonProperty("paymentStatus")
    private String paymentStatus;

    @JsonProperty("partPaymentSchedule")
    private List<PartPaymentSchedule> partPaymentSchedule;

    private final static long serialVersionUID = 5806341522845387410L;

    @JsonProperty( "totalAmount" )
    private String totalAmount;

    @JsonProperty( "amountPaid" )
    private String amountPaid;

    @JsonProperty( "transactionRefNumber" )
    private String transactionRefNumber;

    @JsonProperty( "transactionDate" )
    private String transactionDate;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PaymentInfo() {}

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

    @JsonProperty("cardExpiry")
    public String getCardExpiry() {
        return cardExpiry;
    }

    @JsonProperty("cardExpiry")
    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    @JsonProperty("paymentID")
    public String getPaymentID() {
        return paymentID;
    }

    @JsonProperty("paymentID")
    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    @JsonProperty("accountType")
    public String getAccountType() {
        return accountType;
    }

    @JsonProperty("accountType")
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @JsonProperty("cardType")
    public String getCardType() {
        return cardType;
    }

    @JsonProperty("cardType")
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @JsonProperty("paymentMethod")
    public String getPaymentMethod() {
        return paymentMethod;
    }

    @JsonProperty("paymentMethod")
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @JsonProperty("encryptionKey")
    public String getEncryptionKey() {
        return encryptionKey;
    }

    @JsonProperty("encryptionKey")
    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    @JsonProperty("paymentAmount")
    public String getPaymentAmount() {
        return paymentAmount;
    }

    @JsonProperty("paymentAmount")
    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @JsonProperty("cardNumber")
    public String getCardNumber() {
        return cardNumber;
    }

    @JsonProperty("cardNumber")
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @JsonProperty("paymentType")
    public String getPaymentType() {
        return paymentType;
    }

    @JsonProperty("paymentType")
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @JsonProperty("amountCurrency")
    public String getAmountCurrency() {
        return amountCurrency;
    }

    @JsonProperty("amountCurrency")
    public void setAmountCurrency(String amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<PartPaymentSchedule> getPartPaymentSchedule() {
        return partPaymentSchedule;
    }

    public void setPartPaymentSchedule(List<PartPaymentSchedule> partPaymentSchedule) {
        this.partPaymentSchedule = partPaymentSchedule;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PaymentInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("cardExpiry");
        sb.append('=');
        sb.append(((this.cardExpiry == null)?"<null>":this.cardExpiry));
        sb.append(',');
        sb.append("paymentID");
        sb.append('=');
        sb.append(((this.paymentID == null)?"<null>":this.paymentID));
        sb.append(',');
        sb.append("accountType");
        sb.append('=');
        sb.append(((this.accountType == null)?"<null>":this.accountType));
        sb.append(',');
        sb.append("cardType");
        sb.append('=');
        sb.append(((this.cardType == null)?"<null>":this.cardType));
        sb.append(',');
        sb.append("paymentMethod");
        sb.append('=');
        sb.append(((this.paymentMethod == null)?"<null>":this.paymentMethod));
        sb.append(',');
        sb.append("encryptionKey");
        sb.append('=');
        sb.append(((this.encryptionKey == null)?"<null>":this.encryptionKey));
        sb.append(',');
        sb.append("paymentAmount");
        sb.append('=');
        sb.append(((this.paymentAmount == null)?"<null>":this.paymentAmount));
        sb.append(',');
        sb.append("cardNumber");
        sb.append('=');
        sb.append(((this.cardNumber == null)?"<null>":this.cardNumber));
        sb.append(',');
        sb.append("paymentType");
        sb.append('=');
        sb.append(((this.paymentType == null)?"<null>":this.paymentType));
        sb.append(',');
        sb.append("amountCurrency");
        sb.append('=');
        sb.append(((this.amountCurrency == null)?"<null>":this.amountCurrency));
        sb.append(',');
        sb.append("token");
        sb.append('=');
        sb.append(((this.token == null)?"<null>":this.token));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.cardExpiry == null)? 0 :this.cardExpiry.hashCode()));
        result = ((result* 31)+((this.paymentID == null)? 0 :this.paymentID.hashCode()));
        result = ((result* 31)+((this.accountType == null)? 0 :this.accountType.hashCode()));
        result = ((result* 31)+((this.cardType == null)? 0 :this.cardType.hashCode()));
        result = ((result* 31)+((this.paymentMethod == null)? 0 :this.paymentMethod.hashCode()));
        result = ((result* 31)+((this.encryptionKey == null)? 0 :this.encryptionKey.hashCode()));
        result = ((result* 31)+((this.paymentAmount == null)? 0 :this.paymentAmount.hashCode()));
        result = ((result* 31)+((this.cardNumber == null)? 0 :this.cardNumber.hashCode()));
        result = ((result* 31)+((this.paymentType == null)? 0 :this.paymentType.hashCode()));
        result = ((result* 31)+((this.amountCurrency == null)? 0 :this.amountCurrency.hashCode()));
        result = ((result* 31)+((this.token == null)? 0 :this.token.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PaymentInfo) == false) {
            return false;
        }
        PaymentInfo rhs = ((PaymentInfo) other);
        return ((((((((((((this.cardExpiry == rhs.cardExpiry)||((this.cardExpiry!= null)&&this.cardExpiry.equals(rhs.cardExpiry)))&&((this.paymentID == rhs.paymentID)||((this.paymentID!= null)&&this.paymentID.equals(rhs.paymentID))))&&((this.accountType == rhs.accountType)||((this.accountType!= null)&&this.accountType.equals(rhs.accountType))))&&((this.cardType == rhs.cardType)||((this.cardType!= null)&&this.cardType.equals(rhs.cardType))))&&((this.paymentMethod == rhs.paymentMethod)||((this.paymentMethod!= null)&&this.paymentMethod.equals(rhs.paymentMethod))))&&((this.encryptionKey == rhs.encryptionKey)||((this.encryptionKey!= null)&&this.encryptionKey.equals(rhs.encryptionKey))))&&((this.paymentAmount == rhs.paymentAmount)||((this.paymentAmount!= null)&&this.paymentAmount.equals(rhs.paymentAmount))))&&((this.cardNumber == rhs.cardNumber)||((this.cardNumber!= null)&&this.cardNumber.equals(rhs.cardNumber))))&&((this.paymentType == rhs.paymentType)||((this.paymentType!= null)&&this.paymentType.equals(rhs.paymentType))))&&((this.amountCurrency == rhs.amountCurrency)||((this.amountCurrency!= null)&&this.amountCurrency.equals(rhs.amountCurrency))))&&((this.token == rhs.token)||((this.token!= null)&&this.token.equals(rhs.token))));
    }

}
