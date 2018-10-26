
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
    "tax",
    "currencyCode"
})
public class Taxes implements Serializable
{

    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("tax")
    private List<Tax> tax = new ArrayList<Tax>();
    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("total")
    private Double total;


    private final static long serialVersionUID = -8337625040221324916L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Taxes() {
    }

    /**
     * 
     * @param amount
     * @param tax
     * @param currencyCode
     */
    public Taxes(Double amount, List<Tax> tax, String currencyCode) {
        super();
        this.amount = amount;
        this.tax = tax;
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

    @JsonProperty("tax")
    public List<Tax> getTax() {
        return tax;
    }

    @JsonProperty("tax")
    public void setTax(List<Tax> tax) {
        this.tax = tax;
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
        sb.append(Taxes.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("amount");
        sb.append('=');
        sb.append(((this.amount == null)?"<null>":this.amount));
        sb.append(',');
        sb.append("tax");
        sb.append('=');
        sb.append(((this.tax == null)?"<null>":this.tax));
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
        result = ((result* 31)+((this.tax == null)? 0 :this.tax.hashCode()));
        result = ((result* 31)+((this.currencyCode == null)? 0 :this.currencyCode.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Taxes) == false) {
            return false;
        }
        Taxes rhs = ((Taxes) other);
        return ((((this.amount == rhs.amount)||((this.amount!= null)&&this.amount.equals(rhs.amount)))&&((this.tax == rhs.tax)||((this.tax!= null)&&this.tax.equals(rhs.tax))))&&((this.currencyCode == rhs.currencyCode)||((this.currencyCode!= null)&&this.currencyCode.equals(rhs.currencyCode))));
    }

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
