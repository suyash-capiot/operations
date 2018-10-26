
package com.coxandkings.travel.operations.resource.qcmanagement.client;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "supplier",
        "contractValidity",
        "productName"
})
public class CommonElements {

    @JsonProperty("supplier")
    private String supplier;
    @JsonProperty("contractValidity")
    private String contractValidity;
    @JsonProperty("productName")
    private String productName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("supplier")
    public String getSupplier() {
        return supplier;
    }

    @JsonProperty("supplier")
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @JsonProperty("contractValidity")
    public String getContractValidity() {
        return contractValidity;
    }

    @JsonProperty("contractValidity")
    public void setContractValidity(String contractValidity) {
        this.contractValidity = contractValidity;
    }

    @JsonProperty("productName")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("productName")
    public void setProductName(String productName) {
        this.productName = productName;
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
