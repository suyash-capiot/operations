package com.coxandkings.travel.operations.resource.manageofflinebooking;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "marketingAirline",
    "extendedRPH",
    "availableCount",
    "departureTerminal",
    "journeyDuration",
    "CabinType",
    "destinationLocation",
    "ResBookDesigCode",
    "RPH",
    "originLocation",
    "quoteID",
    "arrivalDate",
    "departureDate",
    "arrivalTerminal",
    "operatingAirline",
    "RefundableIndicator",
    "arrival",
    "departure",
    "arrivalTime",
    "departureTime"
})
public class FlightSegment {

    @JsonProperty("marketingAirline")
    private MarketingAirline marketingAirline;
    @JsonProperty("extendedRPH")
    private String extendedRPH;
    @JsonProperty("availableCount")
    private Integer availableCount;
    @JsonProperty("departureTerminal")
    private String departureTerminal;
    @JsonProperty("journeyDuration")
    private Integer journeyDuration;
    @JsonProperty("CabinType")
    private String cabinType;
    @JsonProperty("destinationLocation")
    private String destinationLocation;
    @JsonProperty("ResBookDesigCode")
    private String resBookDesigCode;
    @JsonProperty("RPH")
    private String rPH;
    @JsonProperty("originLocation")
    private String originLocation;
    @JsonProperty("quoteID")
    private String quoteID;
    @JsonProperty("arrivalDate")
    private String arrivalDate;
    @JsonProperty("departureDate")
    private String departureDate;
    @JsonProperty("arrivalTerminal")
    private String arrivalTerminal;
    @JsonProperty("operatingAirline")
    private OperatingAirline operatingAirline;
    @JsonProperty("RefundableIndicator")
    private Boolean refundableIndicator;
    @JsonProperty("arrival")
    private Arrival arrival;
    @JsonProperty("departure")
    private Departure departure;
    @JsonProperty("arrivalTime")
    private String arrivalTime;
    @JsonProperty("departureTime")
    private String departureTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("marketingAirline")
    public MarketingAirline getMarketingAirline() {
        return marketingAirline;
    }

    @JsonProperty("marketingAirline")
    public void setMarketingAirline(MarketingAirline marketingAirline) {
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

    @JsonProperty("CabinType")
    public String getCabinType() {
        return cabinType;
    }

    @JsonProperty("CabinType")
    public void setCabinType(String cabinType) {
        this.cabinType = cabinType;
    }

    @JsonProperty("destinationLocation")
    public String getDestinationLocation() {
        return destinationLocation;
    }

    @JsonProperty("destinationLocation")
    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    @JsonProperty("ResBookDesigCode")
    public String getResBookDesigCode() {
        return resBookDesigCode;
    }

    @JsonProperty("ResBookDesigCode")
    public void setResBookDesigCode(String resBookDesigCode) {
        this.resBookDesigCode = resBookDesigCode;
    }

    @JsonProperty("RPH")
    public String getRPH() {
        return rPH;
    }

    @JsonProperty("RPH")
    public void setRPH(String rPH) {
        this.rPH = rPH;
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

    @JsonProperty("arrivalDate")
    public String getArrivalDate() {
        return arrivalDate;
    }

    @JsonProperty("arrivalDate")
    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
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
    public OperatingAirline getOperatingAirline() {
        return operatingAirline;
    }

    @JsonProperty("operatingAirline")
    public void setOperatingAirline(OperatingAirline operatingAirline) {
        this.operatingAirline = operatingAirline;
    }

    @JsonProperty("RefundableIndicator")
    public Boolean getRefundableIndicator() {
        return refundableIndicator;
    }

    @JsonProperty("RefundableIndicator")
    public void setRefundableIndicator(Boolean refundableIndicator) {
        this.refundableIndicator = refundableIndicator;
    }

    @JsonProperty("arrival")
    public Arrival getArrival() {
        return arrival;
    }

    @JsonProperty("arrival")
    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    @JsonProperty("departure")
    public Departure getDeparture() {
        return departure;
    }

    @JsonProperty("departure")
    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    @JsonProperty("arrivalTime")
    public String getArrivalTime() {
        return arrivalTime;
    }

    @JsonProperty("arrivalTime")
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @JsonProperty("departureTime")
    public String getDepartureTime() {
        return departureTime;
    }

    @JsonProperty("departureTime")
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
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
