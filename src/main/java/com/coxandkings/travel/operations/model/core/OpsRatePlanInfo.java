package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsRatePlanInfo implements Serializable {

    @JsonProperty("ratePlanName")
    private String ratePlanname;

    @JsonProperty("ratePlanRef")
    private String ratePlanRef;

    @JsonProperty("ratePlanCode")
    private String ratePlanCode;

    @JsonProperty("bookingRef")
    private String bookingRef;

    private final static long serialVersionUID = 2547023344659760861L;

    public OpsRatePlanInfo() {
    }

//    public OpsRatePlanInfo(RatePlanInfo ratePlanInfo) {
//        this.ratePlanname = ratePlanInfo.getRatePlanname();
//        this.ratePlanRef = ratePlanInfo.getRatePlanRef();
//        this.ratePlanCode = ratePlanInfo.getRatePlanCode();
//        this.bookingRef = ratePlanInfo.getBookingRef();
//    }

    public String getRatePlanname() {
        return ratePlanname;
    }

    public void setRatePlanname(String ratePlanname) {
        this.ratePlanname = ratePlanname;
    }

    public String getRatePlanRef() {
        return ratePlanRef;
    }

    public void setRatePlanRef(String ratePlanRef) {
        this.ratePlanRef = ratePlanRef;
    }

    public String getRatePlanCode() {
        return ratePlanCode;
    }

    public void setRatePlanCode(String ratePlanCode) {
        this.ratePlanCode = ratePlanCode;
    }

    public String getBookingRef() {
        return bookingRef;
    }

    public void setBookingRef(String bookingRef) {
        this.bookingRef = bookingRef;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsRatePlanInfo that = (OpsRatePlanInfo) o;
        return Objects.equals(ratePlanname, that.ratePlanname) &&
                Objects.equals(ratePlanRef, that.ratePlanRef) &&
                Objects.equals(ratePlanCode, that.ratePlanCode) &&
                Objects.equals(bookingRef, that.bookingRef);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ratePlanname, ratePlanRef, ratePlanCode, bookingRef);
    }
}
