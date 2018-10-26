
package com.coxandkings.travel.operations.resource.partpaymentmonitor;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "inclOtherCharges",
        "refundable",
        "paymentSchdType",
        "billingAmtAsPer",
        "toBillingAmt",
        "fromBillingAmt",
        "paymentDueDate",
        "_id",
        "value",
        "paymentDue",
        "depositIncl",
        "marketCurrency"
})
public class PaymentSchedule {

    @JsonProperty("inclOtherCharges")
    private Boolean inclOtherCharges;
    @JsonProperty("refundable")
    private Boolean refundable;
    @JsonProperty("paymentSchdType")
    private String paymentSchdType;
    @JsonProperty("billingAmtAsPer")
    private String billingAmtAsPer;
    @JsonProperty("toBillingAmt")
    private String toBillingAmt;
    @JsonProperty("fromBillingAmt")
    private String fromBillingAmt;
    @JsonProperty("paymentDueDate")
    private String paymentDueDate;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("value")
    private PaymentValue value;
    @JsonProperty("paymentDue")
    private PaymentDue paymentDue;
    @JsonProperty("depositIncl")
    private List<String> depositIncl = null;
    @JsonProperty("marketCurrency")
    private PaymentMarketCurrency marketCurrency;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("inclOtherCharges")
    public Boolean getInclOtherCharges() {
        return inclOtherCharges;
    }

    @JsonProperty("inclOtherCharges")
    public void setInclOtherCharges(Boolean inclOtherCharges) {
        this.inclOtherCharges = inclOtherCharges;
    }

    @JsonProperty("refundable")
    public Boolean getRefundable() {
        return refundable;
    }

    @JsonProperty("refundable")
    public void setRefundable(Boolean refundable) {
        this.refundable = refundable;
    }

    @JsonProperty("paymentSchdType")
    public String getPaymentSchdType() {
        return paymentSchdType;
    }

    @JsonProperty("paymentSchdType")
    public void setPaymentSchdType(String paymentSchdType) {
        this.paymentSchdType = paymentSchdType;
    }

    @JsonProperty("billingAmtAsPer")
    public String getBillingAmtAsPer() {
        return billingAmtAsPer;
    }

    @JsonProperty("billingAmtAsPer")
    public void setBillingAmtAsPer(String billingAmtAsPer) {
        this.billingAmtAsPer = billingAmtAsPer;
    }

    @JsonProperty("toBillingAmt")
    public String getToBillingAmt() {
        return toBillingAmt;
    }

    @JsonProperty("toBillingAmt")
    public void setToBillingAmt(String toBillingAmt) {
        this.toBillingAmt = toBillingAmt;
    }

    @JsonProperty("fromBillingAmt")
    public String getFromBillingAmt() {
        return fromBillingAmt;
    }

    @JsonProperty("fromBillingAmt")
    public void setFromBillingAmt(String fromBillingAmt) {
        this.fromBillingAmt = fromBillingAmt;
    }

    @JsonProperty("paymentDueDate")
    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    @JsonProperty("paymentDueDate")
    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("value")
    public PaymentValue getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(PaymentValue value) {
        this.value = value;
    }

    @JsonProperty("paymentDue")
    public PaymentDue getPaymentDue() {
        return paymentDue;
    }

    @JsonProperty("paymentDue")
    public void setPaymentDue(PaymentDue paymentDue) {
        this.paymentDue = paymentDue;
    }

    @JsonProperty("depositIncl")
    public List<String> getDepositIncl() {
        return depositIncl;
    }

    @JsonProperty("depositIncl")
    public void setDepositIncl(List<String> depositIncl) {
        this.depositIncl = depositIncl;
    }

    @JsonProperty("marketCurrency")
    public PaymentMarketCurrency getMarketCurrency() {
        return marketCurrency;
    }

    @JsonProperty("marketCurrency")
    public void setMarketCurrency(PaymentMarketCurrency marketCurrency) {
        this.marketCurrency = marketCurrency;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
