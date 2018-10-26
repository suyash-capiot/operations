
package com.coxandkings.travel.operations.resource.fullcancellation;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "createdAt",
    "companyCancelChargesCurrencyCode",
    "lastModifiedAt",
    "companyCancelCharges",
    "supplierCancelCharges"
})
public class OrderCancellation {

    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("companyCancelChargesCurrencyCode")
    private String companyCancelChargesCurrencyCode;
    @JsonProperty("lastModifiedAt")
    private String lastModifiedAt;
    @JsonProperty("companyCancelCharges")
    private Double companyCancelCharges;
    @JsonProperty("supplierCancelCharges")
    private Double supplierCancelCharges;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("companyCancelChargesCurrencyCode")
    public String getCompanyCancelChargesCurrencyCode() {
        return companyCancelChargesCurrencyCode;
    }

    @JsonProperty("companyCancelChargesCurrencyCode")
    public void setCompanyCancelChargesCurrencyCode(String companyCancelChargesCurrencyCode) {
        this.companyCancelChargesCurrencyCode = companyCancelChargesCurrencyCode;
    }

    @JsonProperty("lastModifiedAt")
    public String getLastModifiedAt() {
        return lastModifiedAt;
    }

    @JsonProperty("lastModifiedAt")
    public void setLastModifiedAt(String lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @JsonProperty("companyCancelCharges")
    public Double getCompanyCancelCharges() {
        return companyCancelCharges;
    }

    @JsonProperty("companyCancelCharges")
    public void setCompanyCancelCharges(Double companyCancelCharges) {
        this.companyCancelCharges = companyCancelCharges;
    }

    @JsonProperty("supplierCancelCharges")
    public Double getSupplierCancelCharges() {
        return supplierCancelCharges;
    }

    @JsonProperty("supplierCancelCharges")
    public void setSupplierCancelCharges(Double supplierCancelCharges) {
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
