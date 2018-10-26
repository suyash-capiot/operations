package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsPaxTypeFareSupplier extends OpsPaxTypeFare {

    @JsonProperty("clientCommercials")
    private List<OpsFlightPaxSupplierCommercial> clientCommercials = new ArrayList<>();

    public OpsPaxTypeFareSupplier() {
    }

    public List<OpsFlightPaxSupplierCommercial> getClientCommercials() {
        return clientCommercials;
    }

    public void setClientCommercials(List<OpsFlightPaxSupplierCommercial> clientCommercials) {
        this.clientCommercials = clientCommercials;
    }
}

