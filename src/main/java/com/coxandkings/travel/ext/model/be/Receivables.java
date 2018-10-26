
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
    "receivable",
    "currencyCode"
})
public class Receivables implements Serializable
{

    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("receivable")
    private List<Receivable> receivable = new ArrayList<Receivable>();
    @JsonProperty("currencyCode")
    private String currencyCode;
    private final static long serialVersionUID = 3550025767263146421L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Receivables() {
    }

    /**
     * 
     * @param amount
     * @param receivable
     * @param currencyCode
     */
    public Receivables(Double amount, List<Receivable> receivable, String currencyCode) {
        super();
        this.amount = amount;
        this.receivable = receivable;
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

    @JsonProperty("receivable")
    public List<Receivable> getReceivable() {
        return receivable;
    }

    @JsonProperty("receivable")
    public void setReceivable(List<Receivable> receivable) {
        this.receivable = receivable;
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
        sb.append(Receivables.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("amount");
        sb.append('=');
        sb.append(((this.amount == null)?"<null>":this.amount));
        sb.append(',');
        sb.append("receivable");
        sb.append('=');
        sb.append(((this.receivable == null)?"<null>":this.receivable));
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
        result = ((result* 31)+((this.receivable == null)? 0 :this.receivable.hashCode()));
        result = ((result* 31)+((this.currencyCode == null)? 0 :this.currencyCode.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Receivables) == false) {
            return false;
        }
        Receivables rhs = ((Receivables) other);
        return ((((this.amount == rhs.amount)||((this.amount!= null)&&this.amount.equals(rhs.amount)))&&((this.receivable == rhs.receivable)||((this.receivable!= null)&&this.receivable.equals(rhs.receivable))))&&((this.currencyCode == rhs.currencyCode)||((this.currencyCode!= null)&&this.currencyCode.equals(rhs.currencyCode))));
    }

}
