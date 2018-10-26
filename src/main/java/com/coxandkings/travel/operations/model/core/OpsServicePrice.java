package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsServicePrice  {

    @JsonProperty("basePrice")
    private String basePrice;

    public OpsServicePrice() {
    }

    public OpsServicePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }
}
