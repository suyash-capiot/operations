
package com.coxandkings.travel.ext.model.be;

import java.util.HashMap;
import java.util.List;
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
    "destinationLocation",
    "rph",
    "cabinType",
    "connectionType",
    "quoteID",
    "originLocation",
    "resBookDesigCode",
    "arrivalDate",
    "fareInfo",
    "refundableIndicator",
    "baggages",
    "departureDate",
    "mealCode",
    "arrivalTerminal",
    "status",
    "operatingAirline"
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
    @JsonProperty("destinationLocation")
    private String destinationLocation;
    @JsonProperty("rph")
    private String rph;
    @JsonProperty("cabinType")
    private String cabinType;
    @JsonProperty("connectionType")
    private String connectionType;
    @JsonProperty("quoteID")
    private String quoteID;
    @JsonProperty("originLocation")
    private String originLocation;
    @JsonProperty("resBookDesigCode")
    private String resBookDesigCode;
    @JsonProperty("arrivalDate")
    private String arrivalDate;
    @JsonProperty("fareInfo")
    private List<FareInfo_> fareInfo = null;
    @JsonProperty("refundableIndicator")
    private Boolean refundableIndicator;
    @JsonProperty("baggages")
    private List<Object> baggages = null;
    @JsonProperty("departureDate")
    private String departureDate;
    @JsonProperty("mealCode")
    private String mealCode;
    @JsonProperty("arrivalTerminal")
    private String arrivalTerminal;
    @JsonProperty("status")
    private String status;
    @JsonProperty("operatingAirline")
    private OperatingAirline operatingAirline;
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

    @JsonProperty("quoteID")
    public String getQuoteID() {
        return quoteID;
    }

    @JsonProperty("quoteID")
    public void setQuoteID(String quoteID) {
        this.quoteID = quoteID;
    }

    @JsonProperty("originLocation")
    public String getOriginLocation() {
        return originLocation;
    }

    @JsonProperty("originLocation")
    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
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
    public List<FareInfo_> getFareInfo() {
        return fareInfo;
    }

    @JsonProperty("fareInfo")
    public void setFareInfo(List<FareInfo_> fareInfo) {
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

    @JsonProperty("baggages")
    public List<Object> getBaggages() {
        return baggages;
    }

    @JsonProperty("baggages")
    public void setBaggages(List<Object> baggages) {
        this.baggages = baggages;
    }

    @JsonProperty("departureDate")
    public String getDepartureDate() {
        return departureDate;
    }

    @JsonProperty("departureDate")
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    @JsonProperty("mealCode")
    public String getMealCode() {
        return mealCode;
    }

    @JsonProperty("mealCode")
    public void setMealCode(String mealCode) {
        this.mealCode = mealCode;
    }

    @JsonProperty("arrivalTerminal")
    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    @JsonProperty("arrivalTerminal")
    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("operatingAirline")
    public OperatingAirline getOperatingAirline() {
        return operatingAirline;
    }

    @JsonProperty("operatingAirline")
    public void setOperatingAirline(OperatingAirline operatingAirline) {
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
