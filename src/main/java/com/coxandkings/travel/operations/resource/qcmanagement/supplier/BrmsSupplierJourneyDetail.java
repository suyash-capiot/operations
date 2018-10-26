
package com.coxandkings.travel.operations.resource.qcmanagement.supplier;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "flightDetails",
    "passengerDetails",
    "flightType",
    "flightLineType",
    "codeShareFlightIncluded",
    "travelProductName",
    "travelDetails"
})
public class BrmsSupplierJourneyDetail {

    @JsonProperty("flightDetails")
    private List<BrmsSupplierFlightDetail> BRMSFlightDetails = null;
    @JsonProperty("passengerDetails")
    private List<BrmsSupplierPassengerDetail> BRMSPassengerDetails = null;
    @JsonProperty("flightType")
    private String flightType;
    @JsonProperty("flightLineType")
    private String flightLineType;
    @JsonProperty("codeShareFlightIncluded")
    private Boolean codeShareFlightIncluded;
    @JsonProperty("travelProductName")
    private String travelProductName;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("flightDetails")
    public List<BrmsSupplierFlightDetail> getBRMSFlightDetails() {
        return BRMSFlightDetails;
    }

    @JsonProperty("flightDetails")
    public void setBRMSFlightDetails(List<BrmsSupplierFlightDetail> BRMSFlightDetails) {
        this.BRMSFlightDetails = BRMSFlightDetails;
    }

    @JsonProperty("passengerDetails")
    public List<BrmsSupplierPassengerDetail> getBRMSPassengerDetails() {
        return BRMSPassengerDetails;
    }

    @JsonProperty("passengerDetails")
    public void setBRMSPassengerDetails(List<BrmsSupplierPassengerDetail> BRMSPassengerDetails) {
        this.BRMSPassengerDetails = BRMSPassengerDetails;
    }

    @JsonProperty("flightType")
    public String getFlightType() {
        return flightType;
    }

    @JsonProperty("flightType")
    public void setFlightType(String flightType) {
        this.flightType = flightType;
    }

    @JsonProperty("flightLineType")
    public String getFlightLineType() {
        return flightLineType;
    }

    @JsonProperty("flightLineType")
    public void setFlightLineType(String flightLineType) {
        this.flightLineType = flightLineType;
    }

    @JsonProperty("codeShareFlightIncluded")
    public Boolean getCodeShareFlightIncluded() {
        return codeShareFlightIncluded;
    }

    @JsonProperty("codeShareFlightIncluded")
    public void setCodeShareFlightIncluded(Boolean codeShareFlightIncluded) {
        this.codeShareFlightIncluded = codeShareFlightIncluded;
    }

    @JsonProperty("travelProductName")
    public String getTravelProductName() {
        return travelProductName;
    }

    @JsonProperty("travelProductName")
    public void setTravelProductName(String travelProductName) {
        this.travelProductName = travelProductName;
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
