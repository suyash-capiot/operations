package com.coxandkings.travel.operations.resource.reconfirmation.response.reconfirmation;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;

import java.time.ZonedDateTime;

public class SupplierReconfirmationResponse  {

    private String reconfirmedBy;
    private String supplierReconfirmationDate;
    private String supplierName;
    private String productName;
    private String city;
    private String country;
    private ReconfirmationBookingAttribute bookingAttribute;
    private boolean rejectedDueToNoResponse;
    private String reconfirmationOnHoldUntilDate;
    private String remarks;
    private String bookingRefNo;
    private String orderID;
    private String supplierReconfirmationID;
    private String productReferenceNumber;

    public String getProductReferenceNumber( ) {
        return productReferenceNumber;
    }

    public void setProductReferenceNumber( String productReferenceNumber ) {
        this.productReferenceNumber = productReferenceNumber;
    }

    public String getReconfirmedBy() {
        return reconfirmedBy;
    }

    public void setReconfirmedBy(String reconfirmedBy) {
        this.reconfirmedBy = reconfirmedBy;
    }

    public String getSupplierReconfirmationDate() {
        return supplierReconfirmationDate;
    }

    public void setSupplierReconfirmationDate(String supplierReconfirmationDate) {
        this.supplierReconfirmationDate = supplierReconfirmationDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ReconfirmationBookingAttribute getBookingAttribute() {
        return bookingAttribute;
    }

    public void setBookingAttribute(ReconfirmationBookingAttribute bookingAttribute) {
        this.bookingAttribute = bookingAttribute;
    }

    public boolean isRejectedDueToNoResponse() {
        return rejectedDueToNoResponse;
    }

    public void setRejectedDueToNoResponse(boolean rejectedDueToNoResponse) {
        this.rejectedDueToNoResponse = rejectedDueToNoResponse;
    }

    public String getReconfirmationOnHoldUntilDate() {
        return reconfirmationOnHoldUntilDate;
    }

    public void setReconfirmationOnHoldUntilDate(String reconfirmationOnHoldUntilDate) {
        this.reconfirmationOnHoldUntilDate = reconfirmationOnHoldUntilDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public SupplierReconfirmationResponse() {

//        reconfirmedBy = "Supplier or ServiceProvider";
//        supplierReconfirmationDate = "";
        if ( supplierName == null ) {
            supplierName = "BONOTEL";
        }

//        productName = "xyz";
//        bookingAttribute = ReconfirmationBookingAttribute.RECONFIRMED;
//        rejectedDueToNoResponse = false;
        if ( supplierReconfirmationDate == null ) {
            supplierReconfirmationDate = ZonedDateTime.now().toOffsetDateTime().toString();
        }
       //reconfirmationOnHoldUntilDate = ZonedDateTime.now().toString();
//        remarks = "some reason";

        if ( productName == null ) {
            productName = "Hotel Hilton";
        }
        if ( city==null) {
            city="mumbai";
        }
        if ( country == null ) {
            country="india";
        }

    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSupplierReconfirmationID() {
        return supplierReconfirmationID;
    }

    public void setSupplierReconfirmationID( String supplierReconfirmationID ) {
        this.supplierReconfirmationID = supplierReconfirmationID;
    }

    public String getCity( ) {
        return city;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public String getCountry( ) {
        return country;
    }

    public void setCountry( String country ) {
        this.country = country;
    }

    @Override
    public String toString( ) {
        return "SupplierReconfirmationResponse{" +
                "reconfirmedBy='" + reconfirmedBy + '\'' +
                ", supplierReconfirmationDate='" + supplierReconfirmationDate + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", productName='" + productName + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", bookingAttribute=" + bookingAttribute +
                ", rejectedDueToNoResponse=" + rejectedDueToNoResponse +
                ", reconfirmationOnHoldUntilDate='" + reconfirmationOnHoldUntilDate + '\'' +
                ", remarks='" + remarks + '\'' +
                ", bookingRefNo='" + bookingRefNo + '\'' +
                ", orderID='" + orderID + '\'' +
                ", supplierReconfirmationID='" + supplierReconfirmationID + '\'' +
                '}';
    }
}
