package com.coxandkings.travel.operations.model.commercials;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "ineligible_commercials")
public class BookingIneligibleFor implements Serializable {

    @Id
    @NotNull(message = "Booking Not Eligible for cannot be null")
    @Column(name  ="booking_not_eligible_for")
    private String bookingNotEligibleFor;


    public BookingIneligibleFor(){
    }

    public BookingIneligibleFor(String bookingNotEligibleFor) {
        this.bookingNotEligibleFor = bookingNotEligibleFor;
    }

    public String getBookingNotEligibleFor() {
        return bookingNotEligibleFor;
    }

    public void setBookingNotEligibleFor(String bookingNotEligibleFor) {
        this.bookingNotEligibleFor = bookingNotEligibleFor;
    }
}
