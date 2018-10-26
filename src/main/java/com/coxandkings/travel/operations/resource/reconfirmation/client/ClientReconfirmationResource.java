package com.coxandkings.travel.operations.resource.reconfirmation.client;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;

import java.time.ZonedDateTime;

public class ClientReconfirmationResource {

    private String clientReconfirmationID;
    private ZonedDateTime clientOrCustomerReconfirmationDate;
    private ReconfirmationBookingAttribute bookingAttribute;
    private boolean rejectedDueToNoResponse;
    private ZonedDateTime reconfirmationOnHoldUntilDate;
    private String remarks;


    public ReconfirmationBookingAttribute getBookingAttribute( ) {
        return bookingAttribute;
    }

    public void setBookingAttribute( ReconfirmationBookingAttribute bookingAttribute ) {
        this.bookingAttribute = bookingAttribute;
    }

    public boolean isRejectedDueToNoResponse( ) {
        return rejectedDueToNoResponse;
    }

    public void setRejectedDueToNoResponse( boolean rejectedDueToNoResponse ) {
        this.rejectedDueToNoResponse = rejectedDueToNoResponse;
    }

    public String getRemarks( ) {
        return remarks;
    }

    public void setRemarks( String remarks ) {
        this.remarks = remarks;
    }



    public String getClientReconfirmationID( ) {
        return clientReconfirmationID;
    }

    public void setClientReconfirmationID( String clientReconfirmationID ) {
        this.clientReconfirmationID = clientReconfirmationID;
    }

    public ZonedDateTime getClientOrCustomerReconfirmationDate( ) {
        return clientOrCustomerReconfirmationDate;
    }

    public void setClientOrCustomerReconfirmationDate( ZonedDateTime clientOrCustomerReconfirmationDate ) {
        this.clientOrCustomerReconfirmationDate = clientOrCustomerReconfirmationDate;
    }

    public ZonedDateTime getReconfirmationOnHoldUntilDate( ) {
        return reconfirmationOnHoldUntilDate;
    }

    public void setReconfirmationOnHoldUntilDate( ZonedDateTime reconfirmationOnHoldUntilDate ) {
        this.reconfirmationOnHoldUntilDate = reconfirmationOnHoldUntilDate;
    }

    @Override
    public String toString() {
        return "ClientReconfirmationResource{" +
                "clientReconfirmationID='" + clientReconfirmationID + '\'' +
                ", clientOrCustomerReconfirmationDate=" + clientOrCustomerReconfirmationDate +
                ", bookingAttribute=" + bookingAttribute +
                ", rejectedDueToNoResponse=" + rejectedDueToNoResponse +
                ", reconfirmationOnHoldUntilDate=" + reconfirmationOnHoldUntilDate +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
