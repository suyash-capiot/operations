package com.coxandkings.travel.operations.enums.reconfirmation;

public enum ReconfirmationBookingAttribute {

    RECONFIRMED( "Reconfirmed" ),
    RECONFIRMATION_PENDING( "Reconfirmation Pending" ),
    RECONFIRMATION_REJECTED( "Reconfirmation Rejected" ),
    RECONFIRMATION_CANCELLED( "Reconfirmation Cancelled" ),
    RECONFIRMATION_ON_HOLD( "Reconfirmation On Hold" );

    private String value;

    ReconfirmationBookingAttribute( String value ) {
        this.value = value;
    }

    public String getValue( ) {
        return this.value;
    }

}
