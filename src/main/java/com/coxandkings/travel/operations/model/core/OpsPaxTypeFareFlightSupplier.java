package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsPaxTypeFareFlightSupplier extends OpsPaxTypeFare {

    @JsonProperty("supplierCommercials")
    private List<OpsFlightPaxSupplierCommercial> supplierCommercials = new ArrayList<>();

    public OpsPaxTypeFareFlightSupplier() {
    }

    public List<OpsFlightPaxSupplierCommercial> getSupplierCommercials() {
        return supplierCommercials;
    }

    public void setSupplierCommercials(List<OpsFlightPaxSupplierCommercial> supplierCommercials) {
        this.supplierCommercials = supplierCommercials;
    }
}

