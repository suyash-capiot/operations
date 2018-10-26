package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsContactInfo implements Serializable {

    @JsonProperty("areaCityCode")
    private String areaCityCode;

    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty("contactType")
    private String contactType;

    @JsonProperty("mobileNo")
    private String mobileNo;

    @JsonProperty("email")
    private String email;

    public OpsContactInfo() {
    }

    public String getAreaCityCode() {
        return areaCityCode;
    }

    public void setAreaCityCode(String areaCityCode) {
        this.areaCityCode = areaCityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsContactInfo that = (OpsContactInfo) o;
        return Objects.equals(areaCityCode, that.areaCityCode) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(contactType, that.contactType) &&
                Objects.equals(mobileNo, that.mobileNo) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(areaCityCode, countryCode, contactType, mobileNo, email);
    }
}
