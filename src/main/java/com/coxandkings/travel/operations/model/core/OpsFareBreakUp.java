package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsFareBreakUp implements Serializable {

    @JsonProperty("baseFare")
    private Double baseFare;

    @JsonProperty("taxDetails")
    private List<OpsTaxDetails> taxDetails = new ArrayList<>();

    public OpsFareBreakUp() {
    }

    @JsonProperty("baseFare")
    public Double getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("taxDetails")
    public List<OpsTaxDetails> getTaxDetails() {
        return taxDetails;
    }

    @JsonProperty("taxDetails")
    public void setTaxDetails(List<OpsTaxDetails> taxDetails) {
        this.taxDetails = taxDetails;
    }

}

