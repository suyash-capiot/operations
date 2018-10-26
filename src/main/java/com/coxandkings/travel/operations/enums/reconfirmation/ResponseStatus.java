package com.coxandkings.travel.operations.enums.reconfirmation;

public enum ResponseStatus {

    ACCEPTED( "ACCEPTED" ),
    REJECTED( "REJECTED" ),
    NO_RESPONSE( "NO_RESPONSE" ),
    ON_HOLD( "ON_HOLD" );

    private String value;

    ResponseStatus( String value ) {
        this.value = value;
    }

    public String getValue( ) {
        return this.value;
    }

    public static ResponseStatus fromString( String value ) {

        for ( ResponseStatus responseStatus : ResponseStatus.values( ) ) {
            if ( responseStatus.getValue( ).equalsIgnoreCase( value ) ) {
                return responseStatus;
            }
        }
        return null;
    }
}
