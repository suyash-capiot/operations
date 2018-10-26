package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "supplierId",
    "supplierName"
})
public class SupplierDetail {

    @JsonProperty("supplierId")
    private String supplierId;
    @JsonProperty("supplierName")
    private String supplierName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("supplierId")
    public String getSupplierId() {
        return supplierId;
    }

    @JsonProperty("supplierId")
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @JsonProperty("supplierName")
    public String getSupplierName() {
        return supplierName;
    }

    @JsonProperty("supplierName")
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
