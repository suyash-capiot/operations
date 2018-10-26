
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "feeCode",
    "currencyCode"
})
public class Fee implements Serializable
{

    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("feeCode")
    private String feeCode;
    @JsonProperty("currencyCode")
    private String currencyCode;
    private final static long serialVersionUID = -7859802526667197385L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Fee() {
    }

    /**
     * 
     * @param amount
     * @param feeCode
     * @param currencyCode
     */
    public Fee(Double amount, String feeCode, String currencyCode) {
        super();
        this.amount = amount;
        this.feeCode = feeCode;
        this.currencyCode = currencyCode;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("feeCode")
    public String getFeeCode() {
        return feeCode;
    }

    @JsonProperty("feeCode")
    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Fee.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("amount");
        sb.append('=');
        sb.append(((this.amount == null)?"<null>":this.amount));
        sb.append(',');
        sb.append("feeCode");
        sb.append('=');
        sb.append(((this.feeCode == null)?"<null>":this.feeCode));
        sb.append(',');
        sb.append("currencyCode");
        sb.append('=');
        sb.append(((this.currencyCode == null)?"<null>":this.currencyCode));
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
        result = ((result* 31)+((this.amount == null)? 0 :this.amount.hashCode()));
        result = ((result* 31)+((this.currencyCode == null)? 0 :this.currencyCode.hashCode()));
        result = ((result* 31)+((this.feeCode == null)? 0 :this.feeCode.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Fee) == false) {
            return false;
        }
        Fee rhs = ((Fee) other);
        return ((((this.amount == rhs.amount)||((this.amount!= null)&&this.amount.equals(rhs.amount)))&&((this.currencyCode == rhs.currencyCode)||((this.currencyCode!= null)&&this.currencyCode.equals(rhs.currencyCode))))&&((this.feeCode == rhs.feeCode)||((this.feeCode!= null)&&this.feeCode.equals(rhs.feeCode))));
    }

}
