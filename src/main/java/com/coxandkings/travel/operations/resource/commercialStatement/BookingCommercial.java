package com.coxandkings.travel.operations.resource.commercialStatement;

import java.util.Set;

public class BookingCommercial {
    private String id;
    private Double bookingCommercialCost;
    private Double bookingCommercialTax;
    private String bookingReference;
    private String bookingDate;
    private Set<PassengerDetails> passengerDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getBookingCommercialCost() {
        return bookingCommercialCost;
    }

    public void setBookingCommercialCost(Double bookingCommercialCost) {
        this.bookingCommercialCost = bookingCommercialCost;
    }

    public Double getBookingCommercialTax() {
        return bookingCommercialTax;
    }

    public void setBookingCommercialTax(Double bookingCommercialTax) {
        this.bookingCommercialTax = bookingCommercialTax;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Set<PassengerDetails> getPassengerDetails() {
        return passengerDetails;
    }

    public void setPassengerDetails(Set<PassengerDetails> passengerDetails) {
        this.passengerDetails = passengerDetails;
    }
}

