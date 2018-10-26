package com.coxandkings.travel.operations.resource.manageofflinebooking;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "areaCityCode",
    "countryCode",
    "contactType",
    "mobileNo",
    "email"
})
public class ContactInfo {

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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("areaCityCode")
    public String getAreaCityCode() {
        return areaCityCode;
    }

    @JsonProperty("areaCityCode")
    public void setAreaCityCode(String areaCityCode) {
        this.areaCityCode = areaCityCode;
    }

    @JsonProperty("countryCode")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("countryCode")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @JsonProperty("contactType")
    public String getContactType() {
        return contactType;
    }

    @JsonProperty("contactType")
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    @JsonProperty("mobileNo")
    public String getMobileNo() {
        return mobileNo;
    }

    @JsonProperty("mobileNo")
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
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
