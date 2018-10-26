package com.coxandkings.travel.operations.model.core;

public enum OpsBookingAttribute {

    PAYMENT_NOT_REALISED( "Payment Not Realised" ),
    PAYMENT_REALISED( "Payment Realised" ),
    PENDING( "Pending" ),
    BOOKING_TYPE_TIME_LIMIT( "Time Limit" ),
    BOOKING_TYPE_PAID_BOOKING("Paid Booking"),
    BF( "Failure Booking" ),
    TF( "Failure Transaction" ),
    CF( "Cancellation Failure" ),
    PAYMENT_TYPE_FULL_PAYMENT( "Full Payment" ),
    PAYMENT_TYPE_PART_PAYMENT( "Part Payment" ),
    PAYMENT_TYPE_NO_PAYMENT( "No Payment" ),
    RAMD( "Request for Amendment" ),
    AF( "Amendment Failure" ),
    AMENDED( "Amended" ),
    RECONFIRMATION_CLIENT_RECONFIRMED( "Client Reconfirmed" ),
    RECONFIRMATION_SUPPLIER_RECONFIRMED( "Supplier Reconfirmed" ),
    RECONFIRMATION_PENDING_SUPPLIER_RECONFIRMATION( "Pending Supplier Reconfirmation" ),
    RECONFIRMATION_PENDING_CLIENT_RECONFIRMATION( "Pending Client Reconfirmation" ),
    RECONFIRMATION_REJECTED_BY_CLIENT( "Reconfirmation Rejected by Client" ),
    RECONFIRMATION_REJECTED_BY_SUPPLIER( "Reconfirmation Rejected by Supplier" ),
    RECONFIRMATION_ON_HOLD_BY_SUPPLIER( "Reconfirmation On Hold by Supplier" ),
    RECONFIRMATION_ON_HOLD_BY_CUSTOMER( "Reconfirmation On Hold by Customer" ),
    QC_STATUS_QC_OK( "QC OK" ),
    QC_STATUS_QC_PENDING( "QC Pending" ),
    QC_STATUS_QC_ERROR( "QC Error" ),
    NO_SHOW( "No Show"),
    WORK_IN_PROGRESS( "Work In Progress" );

    private String bookingAttribute;

    private OpsBookingAttribute(String newAttribute )    {
        bookingAttribute = newAttribute;
    }

    public String getBookingAttribute()    {
        return bookingAttribute;
    }

    public static OpsBookingAttribute fromString(String newAttribute) {
        OpsBookingAttribute aBookingAttribute = null;
        if( newAttribute == null || newAttribute.isEmpty() )  {
            return aBookingAttribute;
        }

        for( OpsBookingAttribute tmpBookingAttribute : OpsBookingAttribute.values() )    {
            if( tmpBookingAttribute.getBookingAttribute().equalsIgnoreCase( newAttribute ))  {
                aBookingAttribute = tmpBookingAttribute;
                break;
            }
        }
        return aBookingAttribute;
    }
}
