package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "productCategory",
        "productSubCategory",
    "inclOrExcl",
    "cruiseBrandName",
    "shipName",
    "cruisePackage",
    "itineraryName",
    "productId",
    "ancillaryName"
})
public class ProductCruise {

    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("productSubCategory")
    private String productSubCategory;
    @JsonProperty("inclOrExcl")
    private String inclOrExcl;
    @JsonProperty("cruiseBrandName")
    private String cruiseBrandName;
    @JsonProperty("shipName")
    private String shipName;
    @JsonProperty("cruisePackage")
    private String cruisePackage;
    @JsonProperty("itineraryName")
    private String itineraryName;
    @JsonProperty("productId")
    private String productId;
    @JsonProperty("ancillaryName")
    private String ancillaryName;
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

    @JsonProperty("cruiseBrandName")
    public String getCruiseBrandName() {
        return cruiseBrandName;
    }

    @JsonProperty("cruiseBrandName")
    public void setCruiseBrandName(String cruiseBrandName) {
        this.cruiseBrandName = cruiseBrandName;
    }

    @JsonProperty("shipName")
    public String getShipName() {
        return shipName;
    }

    @JsonProperty("shipName")
    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    @JsonProperty("cruisePackage")
    public String getCruisePackage() {
        return cruisePackage;
    }

    @JsonProperty("cruisePackage")
    public void setCruisePackage(String cruisePackage) {
        this.cruisePackage = cruisePackage;
    }

    @JsonProperty("itineraryName")
    public String getItineraryName() {
        return itineraryName;
    }

    @JsonProperty("itineraryName")
    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    @JsonProperty("productId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("ancillaryName")
    public String getAncillaryName() {
        return ancillaryName;
    }

    @JsonProperty("ancillaryName")
    public void setAncillaryName(String ancillaryName) {
        this.ancillaryName = ancillaryName;
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
