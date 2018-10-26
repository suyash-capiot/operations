package com.coxandkings.travel.operations.resource.managearrivallist;

import com.coxandkings.travel.operations.resource.BaseResource;

public class ArrivalListFlightItemResource extends BaseResource
{

    private String bookID;
    private String supplierReferenceNumber; //Supplier-reference is pnr.
    private String productCategory;
    private String productSubCategory;
    private String clientType;
    private String groupNameId;
    private String supplierId;              //supplierName
    private String flightNumber;
    private String airLineCode;
    private String originLocation;             // fromSector
    private String destinationLocation;        // toSector
    private String firstName;                   // passengerName
    private String lastName;                    // passengerName
    private String paxType;                     //passengerType
    private String cabinType;               // flightClass
    private String rph;                     // rbd
    private String paxCount;                // totalNumberOfPassengers
    private String ticketingPcc;            // pccHapCredential


//    private String airlineName;    // TODO:  Missing fileld


    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getSupplierReferenceNumber() {
        return supplierReferenceNumber;
    }

    public void setSupplierReferenceNumber(String supplierReferenceNumber) {
        this.supplierReferenceNumber = supplierReferenceNumber;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getGroupNameId() {
        return groupNameId;
    }

    public void setGroupNameId(String groupNameId) {
        this.groupNameId = groupNameId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirLineCode() {
        return airLineCode;
    }

    public void setAirLineCode(String airLineCode) {
        this.airLineCode = airLineCode;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPaxType() {
        return paxType;
    }

    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    public String getCabinType() {
        return cabinType;
    }

    public void setCabinType(String cabinType) {
        this.cabinType = cabinType;
    }

    public String getRph() {
        return rph;
    }

    public void setRph(String rph) {
        this.rph = rph;
    }

    public String getPaxCount() {
        return paxCount;
    }

    public void setPaxCount(String paxCount) {
        this.paxCount = paxCount;
    }

    public String getTicketingPcc() {
        return ticketingPcc;
    }

    public void setTicketingPcc(String ticketingPcc) {
        this.ticketingPcc = ticketingPcc;
    }
}
