package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsAddressDetails  implements Serializable {

    @JsonProperty("zipCode")
    private String zipCode;

    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("stateName")
    private String stateName;

    @JsonProperty("countryName")
    private String countryName;

    @JsonProperty("addressLines")
    private List<String> addressLines = new ArrayList<String>();

    @JsonProperty("state")
    private String state;

    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty("bldgRoom")
    private String bldgRoom;


    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public OpsAddressDetails() {
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<String> getAddressLines() {
        return addressLines;
    }

    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getBldgRoom() {
        return bldgRoom;
    }

    public void setBldgRoom(String bldgRoom) {
        this.bldgRoom = bldgRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsAddressDetails that = (OpsAddressDetails) o;
        return Objects.equals(zipCode, that.zipCode) &&
                Objects.equals(cityName, that.cityName) &&
                Objects.equals(stateName, that.stateName) &&
                Objects.equals(countryName, that.countryName) &&
                Objects.equals(addressLines, that.addressLines) &&
                Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {

        return Objects.hash(zipCode, cityName, stateName, countryName, addressLines, state);
    }


}
