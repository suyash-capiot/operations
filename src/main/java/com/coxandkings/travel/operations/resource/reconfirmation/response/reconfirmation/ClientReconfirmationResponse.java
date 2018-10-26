package com.coxandkings.travel.operations.resource.reconfirmation.response.reconfirmation;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;

public class ClientReconfirmationResponse {

    private String productName;
    private String city;
    private String country;
    private String clientOrCustomerReconfirmationDate;
    private ReconfirmationBookingAttribute bookingAttribute;
    private boolean rejectedDueToNoResponse;
    private String reconfirmationOnHoldUntilDate;
    private String remarks;
    private String bookingRefNo;
    private String orderID;
    private String clientReconfirmationID;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getClientOrCustomerReconfirmationDate() {
        return clientOrCustomerReconfirmationDate;
    }

    public void setClientOrCustomerReconfirmationDate(String clientOrCustomerReconfirmationDate) {
        this.clientOrCustomerReconfirmationDate = clientOrCustomerReconfirmationDate;
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

    public ClientReconfirmationResponse() {

//
//        clientOrCustomerReconfirmationDate = ZonedDateTime.now().toString();
//        bookingAttribute = ReconfirmationBookingAttribute.RECONFIRMED;
//        rejectedDueToNoResponse = false;
//        reconfirmationOnHoldUntilDate = ZonedDateTime.now().toString();
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

    public String getClientReconfirmationID() {
        return clientReconfirmationID;
    }

    public void setClientReconfirmationID( String clientReconfirmationID ) {
        this.clientReconfirmationID = clientReconfirmationID;
    }

    public ReconfirmationBookingAttribute getBookingAttribute() {
        return bookingAttribute;
    }

    public void setBookingAttribute(ReconfirmationBookingAttribute bookingAttribute) {
        this.bookingAttribute = bookingAttribute;
    }

    @Override
    public String toString( ) {
        return "ClientReconfirmationResponse{" +
                "productName='" + productName + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", clientOrCustomerReconfirmationDate='" + clientOrCustomerReconfirmationDate + '\'' +
                ", bookingAttribute=" + bookingAttribute +
                ", rejectedDueToNoResponse=" + rejectedDueToNoResponse +
                ", reconfirmationOnHoldUntilDate='" + reconfirmationOnHoldUntilDate + '\'' +
                ", remarks='" + remarks + '\'' +
                ", bookingRefNo='" + bookingRefNo + '\'' +
                ", orderID='" + orderID + '\'' +
                ", clientReconfirmationID='" + clientReconfirmationID + '\'' +
                '}';
    }
}
