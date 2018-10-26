package com.coxandkings.travel.operations.resource.outbound.be;

import com.fasterxml.jackson.annotation.JsonProperty;

public class B2BContactDetails {
    @JsonProperty("contactCategory")
    private String contactCategory;
    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("productCategorySubType")
    private String productCategorySubType;
    @JsonProperty("contactType")
    private String contactType;
    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("cityCode")
    private String cityCode;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("officialEmail")
    private String officialEmail;
    @JsonProperty("alternateEmail")
    private String alternateEmail;
    @JsonProperty("_id")
    private String id;

    public String getContactCategory() {
        return contactCategory;
    }

    public void setContactCategory(String contactCategory) {
        this.contactCategory = contactCategory;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
