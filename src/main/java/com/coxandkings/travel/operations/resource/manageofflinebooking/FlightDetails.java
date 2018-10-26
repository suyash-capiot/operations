package com.coxandkings.travel.operations.resource.manageofflinebooking;

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
    "airlinePNR",
    "orderSupplierPriceInfo",
    "orderClientTotalPriceInfo",
    "tripIndicator",
    "tripType",
    "GDSPNR",
    "originDestinationOptions",
    "paxInfo",
    "prePaymentRequest",
    "firstReservationCheck"
})
public class FlightDetails {

    @JsonProperty("airlinePNR")
    private String airlinePNR;
    @JsonProperty("orderSupplierPriceInfo")
    private OrderSupplierPriceInfo orderSupplierPriceInfo;
    @JsonProperty("orderClientTotalPriceInfo")
    private OrderClientTotalPriceInfo orderClientTotalPriceInfo;
    @JsonProperty("tripIndicator")
    private String tripIndicator;
    @JsonProperty("tripType")
    private String tripType;
    @JsonProperty("GDSPNR")
    private String gDSPNR;
    @JsonProperty("originDestinationOptions")
    private List<OriginDestinationOption> originDestinationOptions = null;
    @JsonProperty("paxInfo")
    private List<PaxInfo> paxInfo = null;
    @JsonProperty("prePaymentRequest")
    private Boolean prePaymentRequest;
    @JsonProperty("firstReservationCheck")
    private Boolean firstReservationCheck;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("airlinePNR")
    public String getAirlinePNR() {
        return airlinePNR;
    }

    @JsonProperty("airlinePNR")
    public void setAirlinePNR(String airlinePNR) {
        this.airlinePNR = airlinePNR;
    }

    @JsonProperty("orderSupplierPriceInfo")
    public OrderSupplierPriceInfo getOrderSupplierPriceInfo() {
        return orderSupplierPriceInfo;
    }

    @JsonProperty("orderSupplierPriceInfo")
    public void setOrderSupplierPriceInfo(OrderSupplierPriceInfo orderSupplierPriceInfo) {
        this.orderSupplierPriceInfo = orderSupplierPriceInfo;
    }

    @JsonProperty("orderClientTotalPriceInfo")
    public OrderClientTotalPriceInfo getOrderClientTotalPriceInfo() {
        return orderClientTotalPriceInfo;
    }

    @JsonProperty("orderClientTotalPriceInfo")
    public void setOrderClientTotalPriceInfo(OrderClientTotalPriceInfo orderClientTotalPriceInfo) {
        this.orderClientTotalPriceInfo = orderClientTotalPriceInfo;
    }

    @JsonProperty("tripIndicator")
    public String getTripIndicator() {
        return tripIndicator;
    }

    @JsonProperty("tripIndicator")
    public void setTripIndicator(String tripIndicator) {
        this.tripIndicator = tripIndicator;
    }

    @JsonProperty("tripType")
    public String getTripType() {
        return tripType;
    }

    @JsonProperty("tripType")
    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    @JsonProperty("GDSPNR")
    public String getGDSPNR() {
        return gDSPNR;
    }

    @JsonProperty("GDSPNR")
    public void setGDSPNR(String gDSPNR) {
        this.gDSPNR = gDSPNR;
    }

    @JsonProperty("originDestinationOptions")
    public List<OriginDestinationOption> getOriginDestinationOptions() {
        return originDestinationOptions;
    }

    @JsonProperty("originDestinationOptions")
    public void setOriginDestinationOptions(List<OriginDestinationOption> originDestinationOptions) {
        this.originDestinationOptions = originDestinationOptions;
    }

    @JsonProperty("paxInfo")
    public List<PaxInfo> getPaxInfo() {
        return paxInfo;
    }

    @JsonProperty("paxInfo")
    public void setPaxInfo(List<PaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    @JsonProperty("prePaymentRequest")
    public Boolean getPrePaymentRequest() {
        return prePaymentRequest;
    }

    @JsonProperty("prePaymentRequest")
    public void setPrePaymentRequest(Boolean prePaymentRequest) {
        this.prePaymentRequest = prePaymentRequest;
    }

    @JsonProperty("firstReservationCheck")
    public Boolean getFirstReservationCheck() {
        return firstReservationCheck;
    }

    @JsonProperty("firstReservationCheck")
    public void setFirstReservationCheck(Boolean firstReservationCheck) {
        this.firstReservationCheck = firstReservationCheck;
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
