package com.coxandkings.travel.operations.resource.reconfirmation.supplier;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;

import java.time.ZonedDateTime;

public class SupplierReconfirmationResource {

    private String reconfirmedBy;
    private ReconfirmationBookingAttribute bookingAttribute;
    private boolean rejectedDueToNoResponse;
    private String productReferenceNumber;
    private ZonedDateTime reconfirmationOnHoldUntilDate;
    private String remarks;
    private String supplierReconfirmationID;

    public String getSupplierReconfirmationID( ) {
        return supplierReconfirmationID;
    }

    public void setSupplierReconfirmationID( String supplierReconfirmationID ) {
        this.supplierReconfirmationID = supplierReconfirmationID;
    }

    public String getReconfirmedBy( ) {
        return reconfirmedBy;
    }

    public void setReconfirmedBy( String reconfirmedBy ) {
        this.reconfirmedBy = reconfirmedBy;
    }

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

    public ZonedDateTime getReconfirmationOnHoldUntilDate( ) {
        return reconfirmationOnHoldUntilDate;
    }

    public void setReconfirmationOnHoldUntilDate( ZonedDateTime reconfirmationOnHoldUntilDate ) {
        this.reconfirmationOnHoldUntilDate = reconfirmationOnHoldUntilDate;
    }

    public String getRemarks( ) {
        return remarks;
    }

    public void setRemarks( String remarks ) {
        this.remarks = remarks;
    }

    public String getProductReferenceNumber( ) {
        return productReferenceNumber;
    }

    public void setProductReferenceNumber( String productReferenceNumber ) {
        this.productReferenceNumber = productReferenceNumber;
    }

    @Override
    public String toString() {
        return "SupplierReconfirmationResource{" +
                "reconfirmedBy='" + reconfirmedBy + '\'' +
                ", bookingAttribute=" + bookingAttribute +
                ", rejectedDueToNoResponse=" + rejectedDueToNoResponse +
                ", productReferenceNumber='" + productReferenceNumber + '\'' +
                ", reconfirmationOnHoldUntilDate=" + reconfirmationOnHoldUntilDate +
                ", remarks='" + remarks + '\'' +
                ", supplierReconfirmationID='" + supplierReconfirmationID + '\'' +
                '}';
    }
}
