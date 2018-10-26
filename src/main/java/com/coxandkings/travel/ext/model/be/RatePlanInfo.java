
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ratePlanname",
    "ratePlanRef",
    "ratePlanCode",
    "bookingRef"
})
public class RatePlanInfo implements Serializable
{

    @JsonProperty("ratePlanName")
    private String ratePlanname;
    @JsonProperty("ratePlanRef")
    private String ratePlanRef;
    @JsonProperty("ratePlanCode")
    private String ratePlanCode;
    @JsonProperty("bookingRef")
    private String bookingRef;
    private final static long serialVersionUID = -7162105828488116474L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RatePlanInfo() {
    }

    /**
     * 
     * @param ratePlanname
     * @param ratePlanRef
     * @param ratePlanCode
     * @param bookingRef
     */
    public RatePlanInfo(String ratePlanname, String ratePlanRef, String ratePlanCode, String bookingRef) {
        super();
        this.ratePlanname = ratePlanname;
        this.ratePlanRef = ratePlanRef;
        this.ratePlanCode = ratePlanCode;
        this.bookingRef = bookingRef;
    }

    @JsonProperty("ratePlanname")
    public String getRatePlanname() {
        return ratePlanname;
    }

    @JsonProperty("ratePlanname")
    public void setRatePlanname(String ratePlanname) {
        this.ratePlanname = ratePlanname;
    }

    @JsonProperty("ratePlanRef")
    public String getRatePlanRef() {
        return ratePlanRef;
    }

    @JsonProperty("ratePlanRef")
    public void setRatePlanRef(String ratePlanRef) {
        this.ratePlanRef = ratePlanRef;
    }

    @JsonProperty("ratePlanCode")
    public String getRatePlanCode() {
        return ratePlanCode;
    }

    @JsonProperty("ratePlanCode")
    public void setRatePlanCode(String ratePlanCode) {
        this.ratePlanCode = ratePlanCode;
    }

    @JsonProperty("bookingRef")
    public String getBookingRef() {
        return bookingRef;
    }

    @JsonProperty("bookingRef")
    public void setBookingRef(String bookingRef) {
        this.bookingRef = bookingRef;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RatePlanInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("ratePlanname");
        sb.append('=');
        sb.append(((this.ratePlanname == null)?"<null>":this.ratePlanname));
        sb.append(',');
        sb.append("ratePlanRef");
        sb.append('=');
        sb.append(((this.ratePlanRef == null)?"<null>":this.ratePlanRef));
        sb.append(',');
        sb.append("ratePlanCode");
        sb.append('=');
        sb.append(((this.ratePlanCode == null)?"<null>":this.ratePlanCode));
        sb.append(',');
        sb.append("bookingRef");
        sb.append('=');
        sb.append(((this.bookingRef == null)?"<null>":this.bookingRef));
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
        result = ((result* 31)+((this.ratePlanCode == null)? 0 :this.ratePlanCode.hashCode()));
        result = ((result* 31)+((this.bookingRef == null)? 0 :this.bookingRef.hashCode()));
        result = ((result* 31)+((this.ratePlanname == null)? 0 :this.ratePlanname.hashCode()));
        result = ((result* 31)+((this.ratePlanRef == null)? 0 :this.ratePlanRef.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RatePlanInfo) == false) {
            return false;
        }
        RatePlanInfo rhs = ((RatePlanInfo) other);
        return (((((this.ratePlanCode == rhs.ratePlanCode)||((this.ratePlanCode!= null)&&this.ratePlanCode.equals(rhs.ratePlanCode)))&&((this.bookingRef == rhs.bookingRef)||((this.bookingRef!= null)&&this.bookingRef.equals(rhs.bookingRef))))&&((this.ratePlanname == rhs.ratePlanname)||((this.ratePlanname!= null)&&this.ratePlanname.equals(rhs.ratePlanname))))&&((this.ratePlanRef == rhs.ratePlanRef)||((this.ratePlanRef!= null)&&this.ratePlanRef.equals(rhs.ratePlanRef))));
    }

}
