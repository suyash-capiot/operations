package com.coxandkings.travel.operations.resource.manageproductupdates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "bookingID",
        "orderID",
        "changeType",
        "supplierID",
        "supplierName",
        "leadPaxName",
        "fromSector",
        "toSector",
        "flightNumber",
        "departureDateAndTime",
        "arrivalDateAndTime",
        "updatedFlightStatus",
        "confirmationStatus"
})
public class ProductUpdateFlightResource {

    @JsonProperty("id")
    private String id;
    @JsonProperty("bookingID")
    private String bookingID;
    @JsonProperty("orderID")
    private String orderID;
    @JsonProperty("changeType")
    private String changeType;
    @JsonProperty("supplierID")
    private String supplierID;
    @JsonProperty("supplierName")
    private String supplierName;
    @JsonProperty("leadPaxName")
    private String leadPaxName;
    @JsonProperty("fromSector")
    private String fromSector;
    @JsonProperty("toSector")
    private String toSector;
    @JsonProperty("flightNumber")
    private String flightNumber;
    @JsonProperty("departureDateAndTime")
    private String departureDateAndTime;
    @JsonProperty("arrivalDateAndTime")
    private String arrivalDateAndTime;
    @JsonProperty("updatedFlightStatus")
    private String updatedFlightStatus;
    @JsonProperty("confirmationStatus")
    private String confirmationStatus;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

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

    @JsonProperty("changeType")
    public String getChangeType() {
        return changeType;
    }

    @JsonProperty("changeType")
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    @JsonProperty("supplierID")
    public String getSupplierID() {
        return supplierID;
    }

    @JsonProperty("supplierID")
    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    @JsonProperty("supplierName")
    public String getSupplierName() {
        return supplierName;
    }

    @JsonProperty("supplierName")
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public String getUpdatedFlightStatus() {
        return updatedFlightStatus;
    }

    public void setUpdatedFlightStatus(String updatedFlightStatus) {
        this.updatedFlightStatus = updatedFlightStatus;
    }

    public String getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(String confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }
}


