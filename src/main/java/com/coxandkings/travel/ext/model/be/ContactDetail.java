
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "contactInfo"
})
public class ContactDetail implements Serializable
{

    @JsonProperty("contactInfo")
    private ContactInfo contactInfo;
    private final static long serialVersionUID = 7788335900465460827L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContactDetail() {
    }

    /**
     * 
     * @param contactInfo
     */
    public ContactDetail(ContactInfo contactInfo) {
        super();
        this.contactInfo = contactInfo;
    }

    @JsonProperty("contactInfo")
    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    @JsonProperty("contactInfo")
    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ContactDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("contactInfo");
        sb.append('=');
        sb.append(((this.contactInfo == null)?"<null>":this.contactInfo));
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
        result = ((result* 31)+((this.contactInfo == null)? 0 :this.contactInfo.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ContactDetail) == false) {
            return false;
        }
        ContactDetail rhs = ((ContactDetail) other);
        return ((this.contactInfo == rhs.contactInfo)||((this.contactInfo!= null)&&this.contactInfo.equals(rhs.contactInfo)));
    }

}
