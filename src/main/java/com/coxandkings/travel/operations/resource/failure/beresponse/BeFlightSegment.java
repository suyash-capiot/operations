
package com.coxandkings.travel.operations.resource.failure.beresponse;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "marketingAirline",
        "extendedRPH",
        "availableCount",
        "departureTerminal",
        "journeyDuration",
        "destinationLocation",
        "rph",
        "cabinType",
        "connectionType",
        "originLocation",
        "quoteID",
        "resBookDesigCode",
        "arrivalDate",
        "fareInfo",
        "refundableIndicator",
        "departureDate",
        "arrivalTerminal",
        "operatingAirline"
})
public class BeFlightSegment {

    @JsonProperty("marketingAirline")
    private BeMarketingAirline marketingAirline;
    @JsonProperty("extendedRPH")
    private String extendedRPH;
    @JsonProperty("availableCount")
    private Integer availableCount;
    @JsonProperty("departureTerminal")
    private String departureTerminal;
    @JsonProperty("journeyDuration")
    private Integer journeyDuration;
    @JsonProperty("destinationLocation")
    private String destinationLocation;
    @JsonProperty("rph")
    private String rph;
    @JsonProperty("cabinType")
    private String cabinType;
    @JsonProperty("connectionType")
    private String connectionType;
    @JsonProperty("originLocation")
    private String originLocation;
    @JsonProperty("quoteID")
    private String quoteID;
    @JsonProperty("resBookDesigCode")
    private String resBookDesigCode;
    @JsonProperty("arrivalDate")
    private String arrivalDate;
    @JsonProperty("fareInfo")
    private List<BeFareInfo> fareInfo = null;
    @JsonProperty("refundableIndicator")
    private Boolean refundableIndicator;
    @JsonProperty("departureDate")
    private String departureDate;
    @JsonProperty("arrivalTerminal")
    private String arrivalTerminal;
    @JsonProperty("operatingAirline")
    private BeOperatingAirline operatingAirline;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("marketingAirline")
    public BeMarketingAirline getMarketingAirline() {
        return marketingAirline;
    }

    @JsonProperty("marketingAirline")
    public void setMarketingAirline(BeMarketingAirline marketingAirline) {
        this.marketingAirline = marketingAirline;
    }

    @JsonProperty("extendedRPH")
    public String getExtendedRPH() {
        return extendedRPH;
    }

    @JsonProperty("extendedRPH")
    public void setExtendedRPH(String extendedRPH) {
        this.extendedRPH = extendedRPH;
    }

    @JsonProperty("availableCount")
    public Integer getAvailableCount() {
        return availableCount;
    }

    @JsonProperty("availableCount")
    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    @JsonProperty("departureTerminal")
    public String getDepartureTerminal() {
        return departureTerminal;
    }

    @JsonProperty("departureTerminal")
    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }

    @JsonProperty("journeyDuration")
    public Integer getJourneyDuration() {
        return journeyDuration;
    }

    @JsonProperty("journeyDuration")
    public void setJourneyDuration(Integer journeyDuration) {
        this.journeyDuration = journeyDuration;
    }

    @JsonProperty("destinationLocation")
    public String getDestinationLocation() {
        return destinationLocation;
    }

    @JsonProperty("destinationLocation")
    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    @JsonProperty("rph")
    public String getRph() {
        return rph;
    }

    @JsonProperty("rph")
    public void setRph(String rph) {
        this.rph = rph;
    }

    @JsonProperty("cabinType")
    public String getCabinType() {
        return cabinType;
    }

    @JsonProperty("cabinType")
    public void setCabinType(String cabinType) {
        this.cabinType = cabinType;
    }

    @JsonProperty("connectionType")
    public String getConnectionType() {
        return connectionType;
    }

    @JsonProperty("connectionType")
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @JsonProperty("originLocation")
    public String getOriginLocation() {
        return originLocation;
    }

    @JsonProperty("originLocation")
    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    @JsonProperty("quoteID")
    public String getQuoteID() {
        return quoteID;
    }

    @JsonProperty("quoteID")
    public void setQuoteID(String quoteID) {
        this.quoteID = quoteID;
    }

    @JsonProperty("resBookDesigCode")
    public String getResBookDesigCode() {
        return resBookDesigCode;
    }

    @JsonProperty("resBookDesigCode")
    public void setResBookDesigCode(String resBookDesigCode) {
        this.resBookDesigCode = resBookDesigCode;
    }

    @JsonProperty("arrivalDate")
    public String getArrivalDate() {
        return arrivalDate;
    }

    @JsonProperty("arrivalDate")
    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @JsonProperty("fareInfo")
    public List<BeFareInfo> getFareInfo() {
        return fareInfo;
    }

    @JsonProperty("fareInfo")
    public void setFareInfo(List<BeFareInfo> fareInfo) {
        this.fareInfo = fareInfo;
    }

    @JsonProperty("refundableIndicator")
    public Boolean getRefundableIndicator() {
        return refundableIndicator;
    }

    @JsonProperty("refundableIndicator")
    public void setRefundableIndicator(Boolean refundableIndicator) {
        this.refundableIndicator = refundableIndicator;
    }

    @JsonProperty("departureDate")
    public String getDepartureDate() {
        return departureDate;
    }

    @JsonProperty("departureDate")
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    @JsonProperty("arrivalTerminal")
    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    @JsonProperty("arrivalTerminal")
    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    @JsonProperty("operatingAirline")
    public BeOperatingAirline getOperatingAirline() {
        return operatingAirline;
    }

    @JsonProperty("operatingAirline")
    public void setOperatingAirline(BeOperatingAirline operatingAirline) {
        this.operatingAirline = operatingAirline;
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
