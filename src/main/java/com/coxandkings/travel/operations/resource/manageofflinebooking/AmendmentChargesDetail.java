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
    "companyAmendCharges",
    "supplierAmendCharges"
})
public class AmendmentChargesDetail {

    @JsonProperty("companyAmendCharges")
    private String companyAmendCharges;
    @JsonProperty("supplierAmendCharges")
    private String supplierAmendCharges;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("companyAmendCharges")
    public String getCompanyAmendCharges() {
        return companyAmendCharges;
    }

    @JsonProperty("companyAmendCharges")
    public void setCompanyAmendCharges(String companyAmendCharges) {
        this.companyAmendCharges = companyAmendCharges;
    }

    @JsonProperty("supplierAmendCharges")
    public String getSupplierAmendCharges() {
        return supplierAmendCharges;
    }

    @JsonProperty("supplierAmendCharges")
    public void setSupplierAmendCharges(String supplierAmendCharges) {
        this.supplierAmendCharges = supplierAmendCharges;
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
