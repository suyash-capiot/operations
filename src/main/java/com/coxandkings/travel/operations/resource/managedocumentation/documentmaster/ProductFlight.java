package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "productCategory",
        "productSubCategory",
    "inclOrExcl",
    "journeyType",
    "productId",
    "countryFrom",
    "countryTo",
    "cityFrom",
    "cityTo"
})
public class ProductFlight {

    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("productSubCategory")
    private String productSubCategory;
    @JsonProperty("inclOrExcl")
    private String inclOrExcl;
    @JsonProperty("journeyType")
    private List<String> journeyType = null;
    @JsonProperty("productId")
    private String productId;
    @JsonProperty("countryFrom")
    private String countryFrom;
    @JsonProperty("countryTo")
    private String countryTo;
    @JsonProperty("cityFrom")
    private String cityFrom;
    @JsonProperty("cityTo")
    private String cityTo;
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

    @JsonProperty("journeyType")
    public List<String> getJourneyType() {
        return journeyType;
    }

    @JsonProperty("journeyType")
    public void setJourneyType(List<String> journeyType) {
        this.journeyType = journeyType;
    }

    @JsonProperty("productId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("countryFrom")
    public String getCountryFrom() {
        return countryFrom;
    }

    @JsonProperty("countryFrom")
    public void setCountryFrom(String countryFrom) {
        this.countryFrom = countryFrom;
    }

    @JsonProperty("countryTo")
    public String getCountryTo() {
        return countryTo;
    }

    @JsonProperty("countryTo")
    public void setCountryTo(String countryTo) {
        this.countryTo = countryTo;
    }

    @JsonProperty("cityFrom")
    public String getCityFrom() {
        return cityFrom;
    }

    @JsonProperty("cityFrom")
    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    @JsonProperty("cityTo")
    public String getCityTo() {
        return cityTo;
    }

    @JsonProperty("cityTo")
    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
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
