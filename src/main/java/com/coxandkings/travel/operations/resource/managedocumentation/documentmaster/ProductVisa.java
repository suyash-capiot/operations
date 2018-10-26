package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "productCategory",
        "productSubCategory",
    "inclOrExcl",
    "country",
    "duration",
    "entryTpe",
    "purposeOfTravel",
    "passportIssuedIn",
    "citizenShipStatus"
})
public class ProductVisa {

    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("productSubCategory")
    private String productSubCategory;
    @JsonProperty("inclOrExcl")
    private String inclOrExcl;
    @JsonProperty("country")
    private String country;
    @JsonProperty("duration")
    private Duration duration;
    @JsonProperty("entryTpe")
    private String entryTpe;
    @JsonProperty("purposeOfTravel")
    private String purposeOfTravel;
    @JsonProperty("passportIssuedIn")
    private String passportIssuedIn;
    @JsonProperty("citizenShipStatus")
    private String citizenShipStatus;
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

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("duration")
    public Duration getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @JsonProperty("entryTpe")
    public String getEntryTpe() {
        return entryTpe;
    }

    @JsonProperty("entryTpe")
    public void setEntryTpe(String entryTpe) {
        this.entryTpe = entryTpe;
    }

    @JsonProperty("purposeOfTravel")
    public String getPurposeOfTravel() {
        return purposeOfTravel;
    }

    @JsonProperty("purposeOfTravel")
    public void setPurposeOfTravel(String purposeOfTravel) {
        this.purposeOfTravel = purposeOfTravel;
    }

    @JsonProperty("passportIssuedIn")
    public String getPassportIssuedIn() {
        return passportIssuedIn;
    }

    @JsonProperty("passportIssuedIn")
    public void setPassportIssuedIn(String passportIssuedIn) {
        this.passportIssuedIn = passportIssuedIn;
    }

    @JsonProperty("citizenShipStatus")
    public String getCitizenShipStatus() {
        return citizenShipStatus;
    }

    @JsonProperty("citizenShipStatus")
    public void setCitizenShipStatus(String citizenShipStatus) {
        this.citizenShipStatus = citizenShipStatus;
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
