package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsContactDetails implements Serializable {

    @JsonProperty("countryAccessCode")
    private String countryAccessCode;

    @JsonProperty("contactNumber")
    private String contactNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("contactInfo")
    private OpsContactInfo contactInfo;


    public OpsContactDetails() {
    }

    public String getCountryAccessCode() {
        return countryAccessCode;
    }

    public void setCountryAccessCode(String countryAccessCode) {
        this.countryAccessCode = countryAccessCode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OpsContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(OpsContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsContactDetails that = (OpsContactDetails) o;
        return Objects.equals(countryAccessCode, that.countryAccessCode) &&
                Objects.equals(contactNumber, that.contactNumber) &&
                Objects.equals(email, that.email) &&
                Objects.equals(contactInfo, that.contactInfo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(countryAccessCode, contactNumber, email, contactInfo);
    }
}
