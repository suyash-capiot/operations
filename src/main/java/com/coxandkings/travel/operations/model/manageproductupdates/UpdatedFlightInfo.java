package com.coxandkings.travel.operations.model.manageproductupdates;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "updated_flight_info")
public class UpdatedFlightInfo extends BaseModel {

    private String bookingID;
    private String orderID;
    private String changeType;
    private String supplierID;
    private String supplierName;
    private String leadPaxName;
    private String fromSector;
    private String toSector;
    private String flightNumber;
    private String departureDateAndTime;
    private String arrivalDateAndTime;
    private String updatedFlightStatus;
    private String confirmationStatus;

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getLeadPaxName() {
        return leadPaxName;
    }

    public void setLeadPaxName(String leadPaxName) {
        this.leadPaxName = leadPaxName;
    }

    public String getFromSector() {
        return fromSector;
    }

    public void setFromSector(String fromSector) {
        this.fromSector = fromSector;
    }

    public String getToSector() {
        return toSector;
    }

    public void setToSector(String toSector) {
        this.toSector = toSector;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureDateAndTime() {
        return departureDateAndTime;
    }

    public void setDepartureDateAndTime(String departureDateAndTime) {
        this.departureDateAndTime = departureDateAndTime;
    }

    public String getArrivalDateAndTime() {
        return arrivalDateAndTime;
    }

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
