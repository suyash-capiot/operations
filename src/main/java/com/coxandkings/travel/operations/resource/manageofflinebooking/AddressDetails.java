package com.coxandkings.travel.operations.resource.manageofflinebooking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "zipCode",
    "cityName",
    "countryName",
    "addressLines",
    "state"
})
public class AddressDetails {

    @JsonProperty("zipCode")
    private String zipCode;
    @JsonProperty("cityName")
    private String cityName;
    @JsonProperty("countryName")
    private String countryName;
    @JsonProperty("addressLines")
    private List<String> addressLines = null;
    @JsonProperty("state")
    private String state;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("zipCode")
    public String getZipCode() {
        return zipCode;
    }

    @JsonProperty("zipCode")
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @JsonProperty("cityName")
    public String getCityName() {
        return cityName;
    }

    @JsonProperty("cityName")
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @JsonProperty("countryName")
    public String getCountryName() {
        return countryName;
    }

    @JsonProperty("countryName")
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @JsonProperty("addressLines")
    public List<String> getAddressLines() {
        return addressLines;
    }

    @JsonProperty("addressLines")
    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
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
