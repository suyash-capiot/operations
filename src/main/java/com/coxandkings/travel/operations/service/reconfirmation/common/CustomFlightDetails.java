package com.coxandkings.travel.operations.service.reconfirmation.common;

import java.time.ZonedDateTime;

public class CustomFlightDetails {

    private ZonedDateTime travelDate = ZonedDateTime.now( );
    private ZonedDateTime clientReconfirmationDate = ZonedDateTime.now( );
    private ZonedDateTime supplierReconfirmationDate = ZonedDateTime.now( );

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
}
