package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsPaxTypeFareHolidaysSupplier extends OpsPaxTypeFare {

    @JsonProperty("supplierCommercials")
    private List<OpsHolidaysPaxSupplierCommercial> supplierCommercials = new ArrayList<>();

    public OpsPaxTypeFareHolidaysSupplier() {
    }

    public List<OpsHolidaysPaxSupplierCommercial> getSupplierCommercials() {
        return supplierCommercials;
    }

    public void setSupplierCommercials(List<OpsHolidaysPaxSupplierCommercial> supplierCommercials) {
        this.supplierCommercials = supplierCommercials;
    }
}

