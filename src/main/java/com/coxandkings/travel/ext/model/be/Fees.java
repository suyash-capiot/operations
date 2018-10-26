
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "fee",
    "currencyCode"
})
public class Fees implements Serializable
{
	
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("fee")
    private List<Fee> fee = new ArrayList<Fee>();
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("total")
    private  Double total;

    private final static long serialVersionUID = -9140361503865520097L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Fees() {
    }

    /**
     * 
     * @param amount
     * @param fee
     * @param currencyCode
     */
    public Fees(Double amount, List<Fee> fee, String currencyCode) {
        super();
        this.amount = amount;
        this.fee = fee;
        this.currencyCode = currencyCode;
    }

  

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("fee")
    public List<Fee> getFee() {
        return fee;
    }

    @JsonProperty("fee")
    public void setFee(List<Fee> fee) {
        this.fee = fee;
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
        sb.append(Fees.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("amount");
        sb.append('=');
        sb.append(((this.amount == null)?"<null>":this.amount));
        sb.append(',');
        sb.append("fee");
        sb.append('=');
        sb.append(((this.fee == null)?"<null>":this.fee));
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
        result = ((result* 31)+((this.fee == null)? 0 :this.fee.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Fees) == false) {
            return false;
        }
        Fees rhs = ((Fees) other);
        return ((((this.amount == rhs.amount)||((this.amount!= null)&&this.amount.equals(rhs.amount)))&&((this.currencyCode == rhs.currencyCode)||((this.currencyCode!= null)&&this.currencyCode.equals(rhs.currencyCode))))&&((this.fee == rhs.fee)||((this.fee!= null)&&this.fee.equals(rhs.fee))));
    }

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
