package com.coxandkings.travel.operations.resource.failure;

import com.coxandkings.travel.ext.model.be.AirItinerary;
import com.coxandkings.travel.ext.model.be.AirItineraryPricingInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AirResponse {

    private Boolean isReturnJourneyCombined;
    private String supplierRef;
    private AirItinerary airItinerary;
    @JsonIgnore
    private AirItineraryPricingInfo airItineraryPricingInfo;

    public Boolean getReturnJourneyCombined() {
        return isReturnJourneyCombined;
    }

    public void setReturnJourneyCombined(Boolean returnJourneyCombined) {
        isReturnJourneyCombined = returnJourneyCombined;
    }

    public String getSupplierRef() {
        return supplierRef;
    }

    public void setSupplierRef(String supplierRef) {
        this.supplierRef = supplierRef;
    }

    public AirItinerary getAirItinerary() {
        return airItinerary;
    }

    public void setAirItinerary(AirItinerary airItinerary) {
        this.airItinerary = airItinerary;
    }

    public AirItineraryPricingInfo getAirItineraryPricingInfo() {
        return airItineraryPricingInfo;
    }

    public void setAirItineraryPricingInfo(AirItineraryPricingInfo airItineraryPricingInfo) {
        this.airItineraryPricingInfo = airItineraryPricingInfo;
    }
}
