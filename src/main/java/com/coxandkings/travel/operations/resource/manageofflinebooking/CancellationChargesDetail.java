package com.coxandkings.travel.operations.resource.manageofflinebooking;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "companyCancelCharges",
    "supplierCancelCharges"
})
public class CancellationChargesDetail {

    @JsonProperty("companyCancelCharges")
    private String companyCancelCharges;
    @JsonProperty("supplierCancelCharges")
    private String supplierCancelCharges;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("companyCancelCharges")
    public String getCompanyCancelCharges() {
        return companyCancelCharges;
    }

    @JsonProperty("companyCancelCharges")
    public void setCompanyCancelCharges(String companyCancelCharges) {
        this.companyCancelCharges = companyCancelCharges;
    }

    @JsonProperty("supplierCancelCharges")
    public String getSupplierCancelCharges() {
        return supplierCancelCharges;
    }

    @JsonProperty("supplierCancelCharges")
    public void setSupplierCancelCharges(String supplierCancelCharges) {
        this.supplierCancelCharges = supplierCancelCharges;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
