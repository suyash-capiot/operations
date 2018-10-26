package com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

public class DateWisePaymentPercentage implements Comparable<DateWisePaymentPercentage> {

    private Date paymentDueDate;
    private BigDecimal percentage;
    private ZonedDateTime bookingDate;
    private ZonedDateTime travelDate;

    public DateWisePaymentPercentage(Date paymentDueDate, BigDecimal percentage) {
        this.paymentDueDate = paymentDueDate;
        this.percentage = percentage;
    }

    public DateWisePaymentPercentage(Date paymentDueDate, BigDecimal percentage, ZonedDateTime bookingDate, ZonedDateTime travelDate) {
        this.paymentDueDate = paymentDueDate;
        this.percentage = percentage;
        this.bookingDate = bookingDate;
        this.travelDate = travelDate;
    }

    public Date getPaymentDueDate() {

        return paymentDueDate;
    }

    public void setPaymentDueDate(Date paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public ZonedDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(ZonedDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public ZonedDateTime getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(ZonedDateTime travelDate) {
        this.travelDate = travelDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateWisePaymentPercentage that = (DateWisePaymentPercentage) o;
        return Objects.equals(bookingDate, that.bookingDate) &&
                Objects.equals(travelDate, that.travelDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(bookingDate, travelDate);
    }

    @Override
    public int compareTo(DateWisePaymentPercentage o) {
        return 0;
    }
}
