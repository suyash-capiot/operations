package com.coxandkings.travel.operations.enums.product;

public enum OpsProductStatus {

    OK( "Confirmed" ),
    RAMD( "Request for Amendment" ),
    REJ( "Rejected" ),
    RQ("On Request"),
    RXL( "Request for Cancellation" ),
    TKD( "Ticketed" ),
    VCH( "Vouchered" ),
    WL( "Waitlisted" ),
    XL( "Cancelled" );

    private String productStatus;

    private OpsProductStatus( String newStatus ) {
        productStatus = newStatus;
    }

    public String getProductStatus( ) {
        return productStatus;
    }

    public static OpsProductStatus fromString( String newStatus ) {
        OpsProductStatus aStatus = null;
        // TODO
        for ( OpsProductStatus opsTmpProductCategory : OpsProductStatus.values( ) ) {
            if ( opsTmpProductCategory.getProductStatus( ).equalsIgnoreCase( newStatus ) ) {
                aStatus = opsTmpProductCategory;
                break;
            }
        }
        return aStatus;
    }
}
