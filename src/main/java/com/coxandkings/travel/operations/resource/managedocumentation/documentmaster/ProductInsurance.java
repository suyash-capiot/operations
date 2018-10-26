package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "productCategory",
        "productSubCategory",
    "inclOrExcl",
    "insuranceCompanyName",
    "destination",
    "insuranceType"
})
public class ProductInsurance {

    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("productSubCategory")
    private String productSubCategory;
    @JsonProperty("inclOrExcl")
    private String inclOrExcl;
    @JsonProperty("insuranceCompanyName")
    private String insuranceCompanyName;
    @JsonProperty("destination")
    private String destination;
    @JsonProperty("insuranceType")
    private String insuranceType;
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

    @JsonProperty("insuranceCompanyName")
    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    @JsonProperty("insuranceCompanyName")
    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    @JsonProperty("destination")
    public String getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public void setDestination(String destination) {
        this.destination = destination;
    }

    @JsonProperty("insuranceType")
    public String getInsuranceType() {
        return insuranceType;
    }

    @JsonProperty("insuranceType")
    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
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
