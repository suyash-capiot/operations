package com.coxandkings.travel.operations.model.reconfirmation;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationConfigFor;
import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationCutOffType;
import com.coxandkings.travel.operations.helper.enums.BookingType;

import java.time.ZonedDateTime;

public class ReconfirmationRequest {


    private ReconfirmationCutOffType reconfirmationCutOffType;
    private long numberOfDaysOrHours;
    private String reconfirmationToBeSent;
    private boolean isEnableAlternateOptionForBooking;
    private boolean isEnableCancellationBooking;
    private boolean isReconfirmationConfigurationDefined;
    private String bookingRefNumber;
    private boolean isCutOffReached;
    private boolean isCutOffPassed;

    private String productID;
    private String orderID;
    private String supplierEmailId;
    private String clientEmailId;
    private String customerEmailId;
    private ZonedDateTime bookingDate;
    private String clientID;
    private  String clientType;
    private  String bookingStatus;
    private BookingType bookingType;
    private ZonedDateTime reconfirmationCutOffDate;

    private ZonedDateTime clientReconfirmationDate;
    private ZonedDateTime supplierReconfirmationDate;
    private ZonedDateTime travelDate;

    public ZonedDateTime getTravelDate( ) {
        return travelDate;
    }

    public void setTravelDate( ZonedDateTime travelDate ) {
        this.travelDate = travelDate;
    }

    public ZonedDateTime getClientReconfirmationDate( ) {
        return clientReconfirmationDate;
    }

    public void setClientReconfirmationDate( ZonedDateTime clientReconfirmationDate ) {
        this.clientReconfirmationDate = clientReconfirmationDate;
    }

    public ZonedDateTime getSupplierReconfirmationDate( ) {
        return supplierReconfirmationDate;
    }

    public void setSupplierReconfirmationDate( ZonedDateTime supplierReconfirmationDate ) {
        this.supplierReconfirmationDate = supplierReconfirmationDate;
    }

    public ZonedDateTime getReconfirmationCutOffDate() {
        return reconfirmationCutOffDate;
    }

    public void setReconfirmationCutOffDate(ZonedDateTime reconfirmationCutOffDate) {
        this.reconfirmationCutOffDate = reconfirmationCutOffDate;
    }

    public BookingType getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingType bookingType) {
        this.bookingType = bookingType;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public ZonedDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(ZonedDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    private ReconfirmationConfigFor ReconfirmationConfigFor;

    public ReconfirmationConfigFor getReconfirmationConfigFor() {
        return ReconfirmationConfigFor;
    }

    public void setReconfirmationConfigFor(ReconfirmationConfigFor reconfirmationConfigFor) {
        this.ReconfirmationConfigFor = reconfirmationConfigFor;
    }

    public ReconfirmationCutOffType getReconfirmationCutOffType() {
        return reconfirmationCutOffType;
    }

    public void setReconfirmationCutOffType(ReconfirmationCutOffType reconfirmationCutOffType) {
        this.reconfirmationCutOffType = reconfirmationCutOffType;
    }

    public long getNumberOfDaysOrHours() {
        return numberOfDaysOrHours;
    }

    public void setNumberOfDaysOrHours(long numberOfDaysOrHours) {
        this.numberOfDaysOrHours = numberOfDaysOrHours;
    }

    public String getReconfirmationToBeSent() {
        return reconfirmationToBeSent;
    }

    public void setReconfirmationToBeSent(String reconfirmationToBeSent) {
        this.reconfirmationToBeSent = reconfirmationToBeSent;
    }

    public boolean isEnableAlternateOptionForBooking() {
        return isEnableAlternateOptionForBooking;
    }

    public void setEnableAlternateOptionForBooking(boolean enableAlternateOptionForBooking) {
        isEnableAlternateOptionForBooking = enableAlternateOptionForBooking;
    }

    public boolean isEnableCancellationBooking() {
        return isEnableCancellationBooking;
    }

    public void setEnableCancellationBooking(boolean enableCancellationBooking) {
        isEnableCancellationBooking = enableCancellationBooking;
    }

    public boolean isReconfirmationConfigurationDefined() {
        return isReconfirmationConfigurationDefined;
    }

    public void setReconfirmationConfigurationDefined(boolean reconfirmationConfigurationDefined) {
        isReconfirmationConfigurationDefined = reconfirmationConfigurationDefined;
    }

    public String getBookingRefNumber() {
        return bookingRefNumber;
    }

    public void setBookingRefNumber(String bookingRefNumber) {
        this.bookingRefNumber = bookingRefNumber;
    }

    public boolean isCutOffReached() {
        return isCutOffReached;
    }

    public void setCutOffReached(boolean cutOffReached) {
        isCutOffReached = cutOffReached;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSupplierEmailId() {
        return supplierEmailId;
    }

    public void setSupplierEmailId(String supplierEmailId) {
        this.supplierEmailId = supplierEmailId;
    }

    public String getClientEmailId() {
        return clientEmailId;
    }

    public void setClientEmailId(String clientEmailId) {
        this.clientEmailId = clientEmailId;
    }

    public String getCustomerEmailId() {
        return customerEmailId;
    }

    public void setCustomerEmailId(String customerEmailId) {
        this.customerEmailId = customerEmailId;
    }

    public boolean isCutOffPassed() {
        return isCutOffPassed;
    }

    public void setCutOffPassed(boolean cutOffPassed) {
        isCutOffPassed = cutOffPassed;
    }
}
