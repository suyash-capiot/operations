
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "zip",
    "country",
    "city",
    "addrLine2",
    "addrLine1",
    "state"
})
public class AddressDetails implements Serializable
{

    @JsonProperty("zip")
    private String zip;
    @JsonProperty("country")
    private String country;
    @JsonProperty("city")
    private String city;
    @JsonProperty("addrLine2")
    private String addrLine2;
    @JsonProperty("addrLine1")
    private String addrLine1;
    @JsonProperty("state")
    private String state;
    @JsonProperty("bldgRoom")
    private String bldgRoom;
    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("addressLine")
    private String addressLine;

    private final static long serialVersionUID = 5797758646544028909L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public AddressDetails() {
    }

    /**
     * 
     * @param zip
     * @param country
     * @param city
     * @param addrLine2
     * @param addrLine1
     * @param state
     */
    public AddressDetails(String zip, String country, String city, String addrLine2, String addrLine1, String state) {
        super();
        this.zip = zip;
        this.country = country;
        this.city = city;
        this.addrLine2 = addrLine2;
        this.addrLine1 = addrLine1;
        this.state = state;
    }

    @JsonProperty("zip")
    public String getZip() {
        return zip;
    }

    @JsonProperty("zip")
    public void setZip(String zip) {
        this.zip = zip;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("addrLine2")
    public String getAddrLine2() {
        return addrLine2;
    }

    @JsonProperty("addrLine2")
    public void setAddrLine2(String addrLine2) {
        this.addrLine2 = addrLine2;
    }

    @JsonProperty("addrLine1")
    public String getAddrLine1() {
        return addrLine1;
    }

    @JsonProperty("addrLine1")
    public void setAddrLine1(String addrLine1) {
        this.addrLine1 = addrLine1;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    public String getBldgRoom() {
        return bldgRoom;
    }

    public void setBldgRoom(String bldgRoom) {
        this.bldgRoom = bldgRoom;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AddressDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("zip");
        sb.append('=');
        sb.append(((this.zip == null)?"<null>":this.zip));
        sb.append(',');
        sb.append("country");
        sb.append('=');
        sb.append(((this.country == null)?"<null>":this.country));
        sb.append(',');
        sb.append("city");
        sb.append('=');
        sb.append(((this.city == null)?"<null>":this.city));
        sb.append(',');
        sb.append("addrLine2");
        sb.append('=');
        sb.append(((this.addrLine2 == null)?"<null>":this.addrLine2));
        sb.append(',');
        sb.append("addrLine1");
        sb.append('=');
        sb.append(((this.addrLine1 == null)?"<null>":this.addrLine1));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null)?"<null>":this.state));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.zip == null)? 0 :this.zip.hashCode()));
        result = ((result* 31)+((this.country == null)? 0 :this.country.hashCode()));
        result = ((result* 31)+((this.city == null)? 0 :this.city.hashCode()));
        result = ((result* 31)+((this.addrLine2 == null)? 0 :this.addrLine2 .hashCode()));
        result = ((result* 31)+((this.addrLine1 == null)? 0 :this.addrLine1 .hashCode()));
        result = ((result* 31)+((this.state == null)? 0 :this.state.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AddressDetails) == false) {
            return false;
        }
        AddressDetails rhs = ((AddressDetails) other);
        return (((((((this.zip == rhs.zip)||((this.zip!= null)&&this.zip.equals(rhs.zip)))&&((this.country == rhs.country)||((this.country!= null)&&this.country.equals(rhs.country))))&&((this.city == rhs.city)||((this.city!= null)&&this.city.equals(rhs.city))))&&((this.addrLine2 == rhs.addrLine2)||((this.addrLine2 != null)&&this.addrLine2 .equals(rhs.addrLine2))))&&((this.addrLine1 == rhs.addrLine1)||((this.addrLine1 != null)&&this.addrLine1 .equals(rhs.addrLine1))))&&((this.state == rhs.state)||((this.state!= null)&&this.state.equals(rhs.state))));
    }

}
