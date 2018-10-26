
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "taxCode",
    "currencyCode"
})
public class Tax implements Serializable
{

    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("taxCode")
    private String taxCode;
    @JsonProperty("currencyCode")
    private String currencyCode;
    private final static long serialVersionUID = 5987550891314084934L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Tax() {
    }

    /**
     * 
     * @param amount
     * @param taxCode
     * @param currencyCode
     */
    public Tax(Double amount, String taxCode, String currencyCode) {
        super();
        this.amount = amount;
        this.taxCode = taxCode;
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

    @JsonProperty("taxCode")
    public String getTaxCode() {
        return taxCode;
    }

    @JsonProperty("taxCode")
    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
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
        sb.append(Tax.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("amount");
        sb.append('=');
        sb.append(((this.amount == null)?"<null>":this.amount));
        sb.append(',');
        sb.append("taxCode");
        sb.append('=');
        sb.append(((this.taxCode == null)?"<null>":this.taxCode));
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
        result = ((result* 31)+((this.taxCode == null)? 0 :this.taxCode.hashCode()));
        result = ((result* 31)+((this.currencyCode == null)? 0 :this.currencyCode.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tax) == false) {
            return false;
        }
        Tax rhs = ((Tax) other);
        return ((((this.amount == rhs.amount)||((this.amount!= null)&&this.amount.equals(rhs.amount)))&&((this.taxCode == rhs.taxCode)||((this.taxCode!= null)&&this.taxCode.equals(rhs.taxCode))))&&((this.currencyCode == rhs.currencyCode)||((this.currencyCode!= null)&&this.currencyCode.equals(rhs.currencyCode))));
    }

}
