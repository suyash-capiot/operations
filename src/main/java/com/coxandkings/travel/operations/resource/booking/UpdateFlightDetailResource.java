package com.coxandkings.travel.operations.resource.booking;

import com.coxandkings.travel.operations.model.core.FlightPaxLevelUpdateResource;

import java.util.List;

public class UpdateFlightDetailResource {

    String id;
    String bookID;
    String orderID;
    String airlineCode;
    String flightNumber;
    String departureDate;
    String arrivalDate;

    private String gdsPNR;
    private String airlinePNR;
    private String supplierRefNumber;
    private String policyNumber;

    private List<FlightPaxLevelUpdateResource> paxInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<FlightPaxLevelUpdateResource> getPaxInfo() {
        return paxInfo;
    }

    public String getGdsPNR() {
        return gdsPNR;
    }

    public void setGdsPNR(String gdsPNR) {
        this.gdsPNR = gdsPNR;
    }

    public void setPaxInfo(List<FlightPaxLevelUpdateResource> paxInfo) {
        this.paxInfo = paxInfo;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }



    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getSupplierRefNumber() {
        return supplierRefNumber;
    }

    public void setSupplierRefNumber(String supplierRefNumber) {
        this.supplierRefNumber = supplierRefNumber;
    }



    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }


    public String getAirlinePNR() {
        return airlinePNR;
    }

    public void setAirlinePNR(String airlinePNR) {
        this.airlinePNR = airlinePNR;
    }

    @Override
    public String toString() {
        return "UpdateFlightDetailResource{" +
                "id='" + id + '\'' +
                ", bookID='" + bookID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", airlineCode='" + airlineCode + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", gdsPNR='" + gdsPNR + '\'' +
                ", airlinePNR='" + airlinePNR + '\'' +
                ", supplierRefNumber='" + supplierRefNumber + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                ", paxInfo=" + paxInfo +
                '}';
    }
}
