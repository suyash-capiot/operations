package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsTaxDetails implements Serializable{

    @JsonProperty("taxValue")
    private Integer taxValue;

    @JsonProperty("taxName")
    private String taxName;

    @JsonProperty("taxValue")
    public Integer getTaxValue() {
        return taxValue;
    }

    @JsonProperty("taxValue")
    public void setTaxValue(Integer taxValue) {
        this.taxValue = taxValue;
    }

    @JsonProperty("taxName")
    public String getTaxName() {
        return taxName;
    }

    @JsonProperty("taxName")
    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

}
