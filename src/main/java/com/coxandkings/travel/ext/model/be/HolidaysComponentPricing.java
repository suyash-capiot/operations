package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HolidaysComponentPricing {

    @JsonProperty("accomodationDetails")
    private HolidaysComponentPricingStructure accomodationDetails;

    @JsonProperty("transferDetails")
    private HolidaysComponentPricingStructure transferDetails;

    @JsonProperty("insuranceDetails")
    private HolidaysComponentPricingStructure insuranceDetails;

    @JsonProperty("activityDetails")
    private HolidaysComponentPricingStructure activityDetails;

    @JsonProperty("extensionNightsDetails")
    private HolidaysComponentPricingStructure extensionNightsDetails;

    @JsonProperty("extrasDetails")
    private HolidaysComponentPricingStructure extrasDetails;

    public HolidaysComponentPricingStructure getAccomodationDetails() {
        return accomodationDetails;
    }

    public void setAccomodationDetails(HolidaysComponentPricingStructure accomodationDetails) {
        this.accomodationDetails = accomodationDetails;
    }

    public HolidaysComponentPricingStructure getTransferDetails() {
        return transferDetails;
    }

    public void setTransferDetails(HolidaysComponentPricingStructure transferDetails) {
        this.transferDetails = transferDetails;
    }

    public HolidaysComponentPricingStructure getInsuranceDetails() {
        return insuranceDetails;
    }

    public void setInsuranceDetails(HolidaysComponentPricingStructure insuranceDetails) {
        this.insuranceDetails = insuranceDetails;
    }

    public HolidaysComponentPricingStructure getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(HolidaysComponentPricingStructure activityDetails) {
        this.activityDetails = activityDetails;
    }

    public HolidaysComponentPricingStructure getExtensionNightsDetails() {
        return extensionNightsDetails;
    }

    public void setExtensionNightsDetails(HolidaysComponentPricingStructure extensionNightsDetails) {
        this.extensionNightsDetails = extensionNightsDetails;
    }

    public HolidaysComponentPricingStructure getExtrasDetails() {
        return extrasDetails;
    }

    public void setExtrasDetails(HolidaysComponentPricingStructure extrasDetails) {
        this.extrasDetails = extrasDetails;
    }


}
