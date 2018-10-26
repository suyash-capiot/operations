package com.coxandkings.travel.operations.zmock.resource;


import com.coxandkings.travel.ext.model.be.Product;

public class MatchedBookingResource {

    String bookingReferenceNumber;
    String leadPassengerName;
    Product productDetails;
    String bookingStatus;

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }

    public String getLeadPassengerName() {
        return leadPassengerName;
    }

    public void setLeadPassengerName(String leadPassengerName) {
        this.leadPassengerName = leadPassengerName;
    }

    public Product getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(Product productDetails) {
        this.productDetails = productDetails;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return "MatchedBookingResource{" +
                "bookingReferenceNumber='" + bookingReferenceNumber + '\'' +
                ", leadPassengerName='" + leadPassengerName + '\'' +
                ", productDetails=" + productDetails +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
