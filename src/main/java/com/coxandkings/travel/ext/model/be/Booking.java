package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

    @JsonProperty("responseBody")
    private BookingResponseBody bookingResponseBody;

    @JsonProperty("responseHeader")
    private BookingResponseHeader bookingResponseHeader;

    public BookingResponseBody getBookingResponseBody() {
        return bookingResponseBody;
    }

    public void setBookingResponseBody(BookingResponseBody bookingResponseBody) {
        this.bookingResponseBody = bookingResponseBody;
    }

    public BookingResponseHeader getBookingResponseHeader() {
        return bookingResponseHeader;
    }

    public void setBookingResponseHeader(BookingResponseHeader bookingResponseHeader) {
        this.bookingResponseHeader = bookingResponseHeader;
    }

}
