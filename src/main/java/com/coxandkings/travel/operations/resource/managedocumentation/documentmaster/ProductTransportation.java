package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "productCategory",
        "productSubCategory",
    "inclOrExcl",
    "category",
    "transferType"
})
public class ProductTransportation {

    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("productSubCategory")
    private String productSubCategory;
    @JsonProperty("inclOrExcl")
    private String inclOrExcl;
    @JsonProperty("category")
    private String category;
    @JsonProperty("transferType")
    private String transferType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("productCategory")
    public String getProductCategory() {
        return productCategory;
    }

    @JsonProperty("productCategory")
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @JsonProperty("productSubCategory")
    public String getProductSubCategory() {
        return productSubCategory;
    }

    @JsonProperty("productSubCategory")
    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    @JsonProperty("inclOrExcl")
    public String getInclOrExcl() {
        return inclOrExcl;
    }

    @JsonProperty("inclOrExcl")
    public void setInclOrExcl(String inclOrExcl) {
        this.inclOrExcl = inclOrExcl;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("transferType")
    public String getTransferType() {
        return transferType;
    }

    @JsonProperty("transferType")
    public void setTransferType(String transferType) {
        this.transferType = transferType;
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
