package com.coxandkings.travel.ext.model.finance.invoice;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesOffice {
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("companyID")
    private String companyID;
    @JsonProperty("salesOfficeName")
    private String salesOfficeName;
    @JsonProperty("salesOfficerName")
    private String salesOfficerName;
    @JsonProperty("salesOfficerAddress")
    private String salesOfficerAddress;
    @JsonProperty("salesOfficeContactNo")
    private String salesOfficeContactNo;
    @JsonProperty("salesOfficeEmail")
    private String salesOfficeEmail;
    @JsonProperty("salesOfficeCity")
    private String salesOfficeCity;
    @JsonProperty("salesOfficeState")
    private String salesOfficeState;
    @JsonProperty("salesOfficePinCode")
    private String salesOfficePinCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("companyName")
    public String getCompanyName() {
        return companyName;
    }

    @JsonProperty("companyName")
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @JsonProperty("salesOfficeName")
    public String getSalesOfficeName() {
        return salesOfficeName;
    }

    @JsonProperty("salesOfficeName")
    public void setSalesOfficeName(String salesOfficeName) {
        this.salesOfficeName = salesOfficeName;
    }

    @JsonProperty("salesOfficerName")
    public String getSalesOfficerName() {
        return salesOfficerName;
    }

    @JsonProperty("salesOfficerName")
    public void setSalesOfficerName(String salesOfficerName) {
        this.salesOfficerName = salesOfficerName;
    }

    @JsonProperty("salesOfficerAddress")
    public String getSalesOfficerAddress() {
        return salesOfficerAddress;
    }

    @JsonProperty("salesOfficerAddress")
    public void setSalesOfficerAddress(String salesOfficerAddress) {
        this.salesOfficerAddress = salesOfficerAddress;
    }

    @JsonProperty("salesOfficeContactNo")
    public String getSalesOfficeContactNo() {
        return salesOfficeContactNo;
    }

    @JsonProperty("salesOfficeContactNo")
    public void setSalesOfficeContactNo(String salesOfficeContactNo) {
        this.salesOfficeContactNo = salesOfficeContactNo;
    }

    @JsonProperty("salesOfficeEmail")
    public String getSalesOfficeEmail() {
        return salesOfficeEmail;
    }

    @JsonProperty("salesOfficeEmail")
    public void setSalesOfficeEmail(String salesOfficeEmail) {
        this.salesOfficeEmail = salesOfficeEmail;
    }

    @JsonProperty("salesOfficeCity")
    public String getSalesOfficeCity() {
        return salesOfficeCity;
    }

    @JsonProperty("salesOfficeCity")
    public void setSalesOfficeCity(String salesOfficeCity) {
        this.salesOfficeCity = salesOfficeCity;
    }

    @JsonProperty("salesOfficeState")
    public String getSalesOfficeState() {
        return salesOfficeState;
    }

    @JsonProperty("salesOfficeState")
    public void setSalesOfficeState(String salesOfficeState) {
        this.salesOfficeState = salesOfficeState;
    }

    @JsonProperty("salesOfficePinCode")
    public String getSalesOfficePinCode() {
        return salesOfficePinCode;
    }

    @JsonProperty("salesOfficePinCode")
    public void setSalesOfficePinCode(String salesOfficePinCode) {
        this.salesOfficePinCode = salesOfficePinCode;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

}