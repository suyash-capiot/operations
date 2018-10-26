
package com.coxandkings.travel.operations.resource.qcmanagement.client;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "flightDetails",
        "flightType",
        "flightLineType",
        "codeShareFlightIncluded",
        "travelProductName",
        "passengerDetails",
        "travelDetails"
})
public class BrmsClientJourneyDetail {

    @JsonProperty("flightDetails")
    private List<BrmsClientFlightDetail> flightDetails = null;
    @JsonProperty("flightType")
    private String flightType;
    @JsonProperty("flightLineType")
    private String flightLineType;
    @JsonProperty("codeShareFlightIncluded")
    private String codeShareFlightIncluded;
    @JsonProperty("travelProductName")
    private String travelProductName;
    @JsonProperty("passengerDetails")
    private List<BrmsClientPassengerDetail> passengerDetails = null;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("flightDetails")
    public List<BrmsClientFlightDetail> getFlightDetails() {
        return flightDetails;
    }

    @JsonProperty("flightDetails")
    public void setFlightDetails(List<BrmsClientFlightDetail> flightDetails) {
        this.flightDetails = flightDetails;
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
    public String getCodeShareFlightIncluded() {
        return codeShareFlightIncluded;
    }

    @JsonProperty("codeShareFlightIncluded")
    public void setCodeShareFlightIncluded(String codeShareFlightIncluded) {
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

    @JsonProperty("passengerDetails")
    public List<BrmsClientPassengerDetail> getPassengerDetails() {
        return passengerDetails;
    }

    @JsonProperty("passengerDetails")
    public void setPassengerDetails(List<BrmsClientPassengerDetail> passengerDetails) {
        this.passengerDetails = passengerDetails;
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
