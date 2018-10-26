
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "areaCityCode",
    "countryCode",
    "contactType",
    "mobileNo",
    "email"
})
public class ContactInfo implements Serializable
{

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

    private final static long serialVersionUID = 1272859541969424838L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContactInfo() {
    }

    /**
     * 
     * @param areaCityCode
     * @param countryCode
     * @param contactType
     * @param mobileNo
     * @param email
     */
    public ContactInfo(String areaCityCode, String countryCode, String contactType, String mobileNo, String email) {
        super();
        this.areaCityCode = areaCityCode;
        this.countryCode = countryCode;
        this.contactType = contactType;
        this.mobileNo = mobileNo;
        this.email = email;
    }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ContactInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("areaCityCode");
        sb.append('=');
        sb.append(((this.areaCityCode == null)?"<null>":this.areaCityCode));
        sb.append(',');
        sb.append("countryCode");
        sb.append('=');
        sb.append(((this.countryCode == null)?"<null>":this.countryCode));
        sb.append(',');
        sb.append("contactType");
        sb.append('=');
        sb.append(((this.contactType == null)?"<null>":this.contactType));
        sb.append(',');
        sb.append("mobileNo");
        sb.append('=');
        sb.append(((this.mobileNo == null)?"<null>":this.mobileNo));
        sb.append(',');
        sb.append("email");
        sb.append('=');
        sb.append(((this.email == null)?"<null>":this.email));
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
        result = ((result* 31)+((this.areaCityCode == null)? 0 :this.areaCityCode.hashCode()));
        result = ((result* 31)+((this.contactType == null)? 0 :this.contactType.hashCode()));
        result = ((result* 31)+((this.mobileNo == null)? 0 :this.mobileNo.hashCode()));
        result = ((result* 31)+((this.countryCode == null)? 0 :this.countryCode.hashCode()));
        result = ((result* 31)+((this.email == null)? 0 :this.email.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ContactInfo) == false) {
            return false;
        }
        ContactInfo rhs = ((ContactInfo) other);
        return ((((((this.areaCityCode == rhs.areaCityCode)||((this.areaCityCode!= null)&&this.areaCityCode.equals(rhs.areaCityCode)))&&((this.contactType == rhs.contactType)||((this.contactType!= null)&&this.contactType.equals(rhs.contactType))))&&((this.mobileNo == rhs.mobileNo)||((this.mobileNo!= null)&&this.mobileNo.equals(rhs.mobileNo))))&&((this.countryCode == rhs.countryCode)||((this.countryCode!= null)&&this.countryCode.equals(rhs.countryCode))))&&((this.email == rhs.email)||((this.email!= null)&&this.email.equals(rhs.email))));
    }

}
