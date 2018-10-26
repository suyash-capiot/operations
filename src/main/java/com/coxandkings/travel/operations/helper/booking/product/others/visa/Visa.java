package com.coxandkings.travel.operations.helper.booking.product.others.visa;

import com.coxandkings.travel.operations.helper.booking.product.Product;

public class Visa extends Product {
    private String visaCountry;
    private String numberOfEntries;
    private String durationOfStay;
    private String purposeOfVisit;
    private Long validFromDate;
    private Long  validToDate;
    private String visaType;

    public String getVisaCountry() {
        return visaCountry;
    }

    public void setVisaCountry(String visaCountry) {
        this.visaCountry = visaCountry;
    }

    public String getNumberOfEntries() {
        return numberOfEntries;
    }

    public void setNumberOfEntries(String numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }

    public String getDurationOfStay() {
        return durationOfStay;
    }

    public void setDurationOfStay(String durationOfStay) {
        this.durationOfStay = durationOfStay;
    }

    public String getPurposeOfVisit() {
        return purposeOfVisit;
    }

    public void setPurposeOfVisit(String purposeOfVisit) {
        this.purposeOfVisit = purposeOfVisit;
    }

    public Long getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(Long validFromDate) {
        this.validFromDate = validFromDate;
    }

    public Long getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(Long validToDate) {
        this.validToDate = validToDate;
    }

    public String getVisaType() {
        return visaType;
    }

    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }
}
