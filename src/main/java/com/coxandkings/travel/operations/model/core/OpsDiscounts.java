package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "amount",
        "currencyCode"
})
public class OpsDiscounts implements Serializable{

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("discount")
    private List<OpsDiscount> discounts = new ArrayList<OpsDiscount>();

    public OpsDiscounts(){

    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<OpsDiscount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<OpsDiscount> discounts) {
        this.discounts = discounts;
    }
}
