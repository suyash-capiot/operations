package com.coxandkings.travel.operations.resource.manageproductupdates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "bookId",
        "orderId",
        "flightNumber",
        "departureDateAndTime",
        "arrivalDateAndTime"
})
public class CustomerOrClientEmailResource {

    @JsonProperty("bookId")
    private String bookId;
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("flightNumber")
    private String flightNumber;
    @JsonProperty("departureDateAndTime")
    private String departureDateAndTime;
    @JsonProperty("arrivalDateAndTime")
    private String arrivalDateAndTime;


    @JsonProperty("bookId")
    public String getBookId() {
        return bookId;
    }

    @JsonProperty("bookId")
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("flightNumber")
    public String getFlightNumber() {
        return flightNumber;
    }

    @JsonProperty("flightNumber")
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @JsonProperty("departureDateAndTime")
    public String getDepartureDateAndTime() {
        return departureDateAndTime;
    }

    @JsonProperty("departureDateAndTime")
    public void setDepartureDateAndTime(String departureDateAndTime) {
        this.departureDateAndTime = departureDateAndTime;
    }

    @JsonProperty("arrivalDateAndTime")
    public String getArrivalDateAndTime() {
        return arrivalDateAndTime;
    }

    @JsonProperty("arrivalDateAndTime")
    public void setArrivalDateAndTime(String arrivalDateAndTime) {
        this.arrivalDateAndTime = arrivalDateAndTime;
    }
}