package com.coxandkings.travel.operations.service.reconfirmation.common;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;

public class ReconfirmAttributeType {

    private ReconfirmationBookingAttribute bookingAttribute;
    private String bookingAttributeValue;


    public ReconfirmationBookingAttribute getBookingAttribute() {
        return bookingAttribute;
    }

    public void setBookingAttribute(ReconfirmationBookingAttribute bookingAttribute) {
        this.bookingAttribute = bookingAttribute;
    }

    public String getBookingAttributeValue() {
        return bookingAttributeValue;
    }

    public void setBookingAttributeValue(String bookingAttributeValue) {
        this.bookingAttributeValue = bookingAttributeValue;
    }

    public ReconfirmAttributeType(ReconfirmationBookingAttribute bookingAttribute, String bookingAttributeValue) {
        this.bookingAttribute = bookingAttribute;
        this.bookingAttributeValue = bookingAttributeValue;
    }

}
