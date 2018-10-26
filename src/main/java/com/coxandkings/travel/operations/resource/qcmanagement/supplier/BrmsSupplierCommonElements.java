package com.coxandkings.travel.operations.resource.qcmanagement.supplier;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "supplier",
        "supplierMarket",
        "productCategorySubType",
        "contractValidity",
        "clientType"
})
public class BrmsSupplierCommonElements {

    @JsonProperty("supplier")
    private String supplier;
    @JsonProperty("supplierMarket")
    private String supplierMarket;
    @JsonProperty("productCategorySubType")
    private String productCategorySubType;
    @JsonProperty("contractValidity")
    private String contractValidity;
    @JsonProperty("clientType")
    private String clientType;
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

    @JsonProperty("supplierMarket")
    public String getSupplierMarket() {
        return supplierMarket;
    }

    @JsonProperty("supplierMarket")
    public void setSupplierMarket(String supplierMarket) {
        this.supplierMarket = supplierMarket;
    }

    @JsonProperty("productCategorySubType")
    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    @JsonProperty("productCategorySubType")
    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    @JsonProperty("contractValidity")
    public String getContractValidity() {
        return contractValidity;
    }

    @JsonProperty("contractValidity")
    public void setContractValidity(String contractValidity) {
        this.contractValidity = contractValidity;
    }

    @JsonProperty("clientType")
    public String getClientType() {
        return clientType;
    }

    @JsonProperty("clientType")
    public void setClientType(String clientType) {
        this.clientType = clientType;
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
