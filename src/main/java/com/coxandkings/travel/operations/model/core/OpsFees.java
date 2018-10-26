package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsFees  implements Serializable  {

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("fee")
    private List<OpsFee> fee = new ArrayList<OpsFee>();

    @JsonProperty("amount")
    private Double total;

    public OpsFees() {
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<OpsFee> getFee() {
        return fee;
    }

    public void setFee(List<OpsFee> fee) {
        this.fee = fee;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal( Double total ) {
        this.total = total;
    }

}
