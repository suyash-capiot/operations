
package com.coxandkings.travel.operations.resource.failure.beresponse;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "isReturnJourneyCombined",
        "airItinerary",
        "supplierRef",
        "airItineraryPricingInfo"
})
public class BePricedItinerary {

    @JsonProperty("isReturnJourneyCombined")
    private Boolean isReturnJourneyCombined;
    @JsonProperty("airItinerary")
    private BeAirItinerary airItinerary;
    @JsonProperty("supplierRef")
    private String supplierRef;
    @JsonProperty("airItineraryPricingInfo")
    private BeAirItineraryPricingInfo airItineraryPricingInfo;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("isReturnJourneyCombined")
    public Boolean getIsReturnJourneyCombined() {
        return isReturnJourneyCombined;
    }

    @JsonProperty("isReturnJourneyCombined")
    public void setIsReturnJourneyCombined(Boolean isReturnJourneyCombined) {
        this.isReturnJourneyCombined = isReturnJourneyCombined;
    }

    @JsonProperty("airItinerary")
    public BeAirItinerary getAirItinerary() {
        return airItinerary;
    }

    @JsonProperty("airItinerary")
    public void setAirItinerary(BeAirItinerary airItinerary) {
        this.airItinerary = airItinerary;
    }

    @JsonProperty("supplierRef")
    public String getSupplierRef() {
        return supplierRef;
    }

    @JsonProperty("supplierRef")
    public void setSupplierRef(String supplierRef) {
        this.supplierRef = supplierRef;
    }

    @JsonProperty("airItineraryPricingInfo")
    public BeAirItineraryPricingInfo getAirItineraryPricingInfo() {
        return airItineraryPricingInfo;
    }

    @JsonProperty("airItineraryPricingInfo")
    public void setAirItineraryPricingInfo(BeAirItineraryPricingInfo airItineraryPricingInfo) {
        this.airItineraryPricingInfo = airItineraryPricingInfo;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
