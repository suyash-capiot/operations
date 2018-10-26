package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsFlightSegment implements Serializable {

    @JsonProperty("marketingAirline")
    private OpsMarketingAirline marketingAirline;

    @JsonProperty("extendedRPH")
    private String extendedRPH;

    @JsonProperty("availableCount")
    private Integer availableCount;

    @JsonProperty("departureTerminal")
    private String departureTerminal;

    @JsonProperty("journeyDuration")
    private Double journeyDuration;

    @JsonProperty("CabinType")
    private String cabinType;

    @JsonProperty("destinationLocation")
    private String destinationLocation;

    @JsonProperty("connectionType")
    private String connectionType;

    @JsonProperty("ResBookDesigCode")
    private String resBookDesigCode;

    @JsonProperty("RPH")
    private String rPH;

    @JsonProperty("status")
    private String status;

    @JsonProperty("originLocation")
    private String originLocation;

    @JsonProperty("fareInfo")
    private List<OpsFareInfo_> fareInfo;

    @JsonProperty("quoteID")
    private String quoteID;

    @JsonProperty("arrivalDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime arrivalDateZDT;

    @JsonProperty("departureDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime departureDateZDT;

    @JsonProperty("arrivalTerminal")
    private String arrivalTerminal;

    @JsonProperty("operatingAirline")
    private OpsOperatingAirline operatingAirline;

    @JsonProperty("RefundableIndicator")
    private Boolean refundableIndicator;

    @JsonProperty("amendmentDateAndTime")
    private String amendmentDateAndTime;

    @JsonProperty("cancellationDateAndTime")
    private String cancellationDateAndTime;

    public OpsFlightSegment() {
    }


    public OpsMarketingAirline getMarketingAirline() {
        return marketingAirline;
    }

    public void setMarketingAirline(OpsMarketingAirline marketingAirline) {
        this.marketingAirline = marketingAirline;
    }

    public String getExtendedRPH() {
        return extendedRPH;
    }

    public void setExtendedRPH(String extendedRPH) {
        this.extendedRPH = extendedRPH;
    }

    public Integer getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }

    public Double getJourneyDuration() {
        return journeyDuration;
    }

    public void setJourneyDuration(Double journeyDuration) {
        this.journeyDuration = journeyDuration;
    }

    public String getCabinType() {
        return cabinType;
    }

    public void setCabinType(String cabinType) {
        this.cabinType = cabinType;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getResBookDesigCode() {
        return resBookDesigCode;
    }

    public void setResBookDesigCode(String resBookDesigCode) {
        this.resBookDesigCode = resBookDesigCode;
    }

    public String getrPH() {
        return rPH;
    }

    public void setrPH(String rPH) {
        this.rPH = rPH;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public String getQuoteID() {
        return quoteID;
    }

    public void setQuoteID(String quoteID) {
        this.quoteID = quoteID;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    public Boolean getRefundableIndicator() {
        return refundableIndicator;
    }

    public OpsOperatingAirline getOperatingAirline() {
        return operatingAirline;
    }

    public void setOperatingAirline(OpsOperatingAirline operatingAirline) {
        this.operatingAirline = operatingAirline;
    }

    public void setRefundableIndicator(Boolean refundableIndicator) {
        this.refundableIndicator = refundableIndicator;
    }

    public ZonedDateTime getArrivalDateZDT() {
        return arrivalDateZDT;
    }

    public void setArrivalDateZDT(ZonedDateTime arrivalDateZDT) {
        this.arrivalDateZDT = arrivalDateZDT;
    }

    public ZonedDateTime getDepartureDateZDT() {
        return departureDateZDT;
    }

    public void setDepartureDateZDT(ZonedDateTime departureDateZDT) {
        this.departureDateZDT = departureDateZDT;
    }

    public String getAmendmentDateAndTime() {
        return amendmentDateAndTime;
    }

    public void setAmendmentDateAndTime(String amendmentDateAndTime) {
        this.amendmentDateAndTime = amendmentDateAndTime;
    }

    public String getCancellationDateAndTime() {
        return cancellationDateAndTime;
    }

    public void setCancellationDateAndTime(String cancellationDateAndTime) {
        this.cancellationDateAndTime = cancellationDateAndTime;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public List<OpsFareInfo_> getFareInfo() {
        return fareInfo;
    }

    public void setFareInfo(List<OpsFareInfo_> fareInfo) {
        this.fareInfo = fareInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
