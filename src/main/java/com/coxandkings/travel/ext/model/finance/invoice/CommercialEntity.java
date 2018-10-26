package com.coxandkings.travel.ext.model.finance.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommercialEntity implements Serializable {
    private String commercialName;
    private String commercialType;
    private Double commercialAmount;

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(String commercialType) {
        this.commercialType = commercialType;
    }

    public Double getCommercialAmount() {
        return commercialAmount;
    }

    public void setCommercialAmount(Double commercialAmount) {
        this.commercialAmount = commercialAmount;
    }
}
