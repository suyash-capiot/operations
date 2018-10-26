package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaysPriceDetails {
    @JsonProperty("tourPrice")
    private OpsHolidaysTourPrice tourPrice;
    @JsonProperty("totalNetPrice")
    private OpsHolidaysTourPrice totalNetPrice;
    @JsonProperty("commision")
    private String commision;
    @JsonProperty("discount")
    private String discount;
    @JsonProperty("companyCancellationCharges")
    private String companyCancellationCharges;
    @JsonProperty("companyAmendmentCharges")
    private String companyAmendmentCharges;
    @JsonProperty("actualMarginAmount")
    private String actualMarginAmount;

    public OpsHolidaysPriceDetails() {
    }

    public OpsHolidaysTourPrice getTourPrice() {
        return tourPrice;
    }

    public void setTourPrice(OpsHolidaysTourPrice tourPrice) {
        this.tourPrice = tourPrice;
    }

    public OpsHolidaysTourPrice getTotalNetPrice() {
        return totalNetPrice;
    }

    public void setTotalNetPrice(OpsHolidaysTourPrice totalNetPrice) {
        this.totalNetPrice = totalNetPrice;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCompanyCancellationCharges() {
        return companyCancellationCharges;
    }

    public void setCompanyCancellationCharges(String companyCancellationCharges) {
        this.companyCancellationCharges = companyCancellationCharges;
    }

    public String getCompanyAmendmentCharges() {
        return companyAmendmentCharges;
    }

    public void setCompanyAmendmentCharges(String companyAmendmentCharges) {
        this.companyAmendmentCharges = companyAmendmentCharges;
    }

    public String getActualMarginAmount() {
        return actualMarginAmount;
    }

    public void setActualMarginAmount(String actualMarginAmount) {
        this.actualMarginAmount = actualMarginAmount;
    }
}
