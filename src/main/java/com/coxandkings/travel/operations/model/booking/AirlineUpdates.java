package com.coxandkings.travel.operations.model.booking;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "AirlineUpdates")
public class AirlineUpdates {


    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String airlineUpdateId;*/

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="airlineUpdateId")
    private String airlineUpdateId;
    private String bookID;
    private String orderID;
    @Lob
    private String flightDetails;
    private String customerResponse;

    @ColumnDefault("false")
    private Boolean isBookingUpdated;

    public String getAirlineUpdateId() {
        return airlineUpdateId;
    }

    public void setAirlineUpdateId(String airlineUpdateId) {
        this.airlineUpdateId = airlineUpdateId;
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

    public String getFlightDetails() {
        return flightDetails;
    }

    public void setFlightDetails(String flightDetails) {
        this.flightDetails = flightDetails;
    }

    public String getCustomerResponse() {
        return customerResponse;
    }

    public void setCustomerResponse(String customerResponse) {
        this.customerResponse = customerResponse;
    }

    public Boolean getBookingUpdated() {
        return isBookingUpdated;
    }

    public void setBookingUpdated(Boolean bookingUpdated) {
        isBookingUpdated = bookingUpdated;
    }
}
