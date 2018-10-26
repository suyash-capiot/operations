package com.coxandkings.travel.operations.resource.manageproductupdates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "bookingID",
        "orderID",
        "supplierID",
        "leadPaxName",
        "fromSector",
        "toSector",
        "flightNumber",
        "departureDate",
        "departureTime",
        "arrivalDate",
        "arrivalTime"
})
public class ProductUpdateFlightInfo {

    @JsonProperty("bookingID")
    private String bookingID;
    @JsonProperty("orderID")
    private String orderID;
    @JsonProperty("supplierID")
    private String supplierID;
    @JsonProperty("leadPaxName")
    private String leadPaxName;
    @JsonProperty("fromSector")
    private String fromSector;
    @JsonProperty("toSector")
    private String toSector;
    @JsonProperty("flightNumber")
    private String flightNumber;
    @JsonProperty("departureDate")
    private String departureDate;
    @JsonProperty("departureTime")
    private String departureTime;
    @JsonProperty("arrivalDate")
    private String arrivalDate;
    @JsonProperty("arrivalTime")
    private String arrivalTime;

    @JsonProperty("bookingID")
    public String getBookingID() {
        return bookingID;
    }

    @JsonProperty("bookingID")
    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    @JsonProperty("orderID")
    public String getOrderID() {
        return orderID;
    }

    @JsonProperty("orderID")
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @JsonProperty("supplierID")
    public String getSupplierID() {
        return supplierID;
    }

    @JsonProperty("supplierID")
    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    @JsonProperty("leadPaxName")
    public String getLeadPaxName() {
        return leadPaxName;
    }

    @JsonProperty("leadPaxName")
    public void setLeadPaxName(String leadPaxName) {
        this.leadPaxName = leadPaxName;
    }

    @JsonProperty("fromSector")
    public String getFromSector() {
        return fromSector;
    }

    @JsonProperty("fromSector")
    public void setFromSector(String fromSector) {
        this.fromSector = fromSector;
    }

    @JsonProperty("toSector")
    public String getToSector() {
        return toSector;
    }

    @JsonProperty("toSector")
    public void setToSector(String toSector) {
        this.toSector = toSector;
    }

    @JsonProperty("flightNumber")
    public String getFlightNumber() {
        return flightNumber;
    }

    @JsonProperty("flightNumber")
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @JsonProperty("departureDate")
    public String getDepartureDate() {
        return departureDate;
    }

    @JsonProperty("departureDate")
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    @JsonProperty("departureTime")
    public String getDepartureTime() {
        return departureTime;
    }

    @JsonProperty("departureTime")
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    @JsonProperty("arrivalDate")
    public String getArrivalDate() {
        return arrivalDate;
    }

    @JsonProperty("arrivalDate")
    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @JsonProperty("arrivalTime")
    public String getArrivalTime() {
        return arrivalTime;
    }

    @JsonProperty("arrivalTime")
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}