package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "productCategory",
        "productSubCategory",
    "inclOrExcl",
    "packageType",
    "flavourType",
    "brand",
    "destination",
    "country",
    "productId",
    "productFlavourName"
})
public class ProductHoliday {

    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("productSubCategory")
    private String productSubCategory;
    @JsonProperty("inclOrExcl")
    private String inclOrExcl;
    @JsonProperty("packageType")
    private String packageType;
    @JsonProperty("flavourType")
    private String flavourType;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("destination")
    private Destination destination;
    @JsonProperty("country")
    private String country;
    @JsonProperty("productId")
    private String productId;
    @JsonProperty("productFlavourName")
    private String productFlavourName;
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

    @JsonProperty("packageType")
    public String getPackageType() {
        return packageType;
    }

    @JsonProperty("packageType")
    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    @JsonProperty("flavourType")
    public String getFlavourType() {
        return flavourType;
    }

    @JsonProperty("flavourType")
    public void setFlavourType(String flavourType) {
        this.flavourType = flavourType;
    }

    @JsonProperty("brand")
    public String getBrand() {
        return brand;
    }

    @JsonProperty("brand")
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @JsonProperty("destination")
    public Destination getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("productId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("productFlavourName")
    public String getProductFlavourName() {
        return productFlavourName;
    }

    @JsonProperty("productFlavourName")
    public void setProductFlavourName(String productFlavourName) {
        this.productFlavourName = productFlavourName;
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
