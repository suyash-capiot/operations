package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsReceivables implements Serializable {

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("receivable")
    private List<OpsReceivable> receivable = new ArrayList<OpsReceivable>();

    private final static long serialVersionUID = -192999777224380432L;


    public OpsReceivables() {
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<OpsReceivable> getReceivable() {
        return receivable;
    }

    public void setReceivable(List<OpsReceivable> receivable) {
        this.receivable = receivable;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}

