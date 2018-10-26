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
    "orderClientCommercials",
    "flightDetails",
    "orderSupplierCommercials",
    "orderStatus",
    "bookingAttributes",
    "bookingAttributeValues",
    "supplierType"
})
public class OrderDetails {

    @JsonProperty("orderClientCommercials")
    private List<OrderClientCommercial> orderClientCommercials = null;
    @JsonProperty("flightDetails")
    private FlightDetails flightDetails;
    @JsonProperty("orderSupplierCommercials")
    private List<OrderSupplierCommercial> orderSupplierCommercials = null;
    @JsonProperty("orderStatus")
    private String orderStatus;
    @JsonProperty("bookingAttributes")
    private List<String> bookingAttributes = null;
    @JsonProperty("bookingAttributeValues")
    private List<String> bookingAttributeValues = null;
    @JsonProperty("supplierType")
    private String supplierType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("orderClientCommercials")
    public List<OrderClientCommercial> getOrderClientCommercials() {
        return orderClientCommercials;
    }

    @JsonProperty("orderClientCommercials")
    public void setOrderClientCommercials(List<OrderClientCommercial> orderClientCommercials) {
        this.orderClientCommercials = orderClientCommercials;
    }

    @JsonProperty("flightDetails")
    public FlightDetails getFlightDetails() {
        return flightDetails;
    }

    @JsonProperty("flightDetails")
    public void setFlightDetails(FlightDetails flightDetails) {
        this.flightDetails = flightDetails;
    }

    @JsonProperty("orderSupplierCommercials")
    public List<OrderSupplierCommercial> getOrderSupplierCommercials() {
        return orderSupplierCommercials;
    }

    @JsonProperty("orderSupplierCommercials")
    public void setOrderSupplierCommercials(List<OrderSupplierCommercial> orderSupplierCommercials) {
        this.orderSupplierCommercials = orderSupplierCommercials;
    }

    @JsonProperty("orderStatus")
    public String getOrderStatus() {
        return orderStatus;
    }

    @JsonProperty("orderStatus")
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @JsonProperty("bookingAttributes")
    public List<String> getBookingAttributes() {
        return bookingAttributes;
    }

    @JsonProperty("bookingAttributes")
    public void setBookingAttributes(List<String> bookingAttributes) {
        this.bookingAttributes = bookingAttributes;
    }

    @JsonProperty("bookingAttributeValues")
    public List<String> getBookingAttributeValues() {
        return bookingAttributeValues;
    }

    @JsonProperty("bookingAttributeValues")
    public void setBookingAttributeValues(List<String> bookingAttributeValues) {
        this.bookingAttributeValues = bookingAttributeValues;
    }

    @JsonProperty("supplierType")
    public String getSupplierType() {
        return supplierType;
    }

    @JsonProperty("supplierType")
    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
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