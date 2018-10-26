package com.coxandkings.travel.operations.enums.refund;

public enum ReasonForRequest {
    NO_SHOW("No Show"),
    CANCELLATION("Cancellation"),
    BOOKING_FAILURE("Booking Failure"),
    TRANSACTION_FAILURE("Transaction Failure"),
    COMPENSATION("Compensation"),
    ALTERNATE_OPTIONS("Alternate Options"),
    AMEND_CLIENT_COMMERCIALS("Amend Client Commercials"),
    AMEND_COMPANY_COMMERCIALS("Amend Company Commercials"),
    AMEND_SUPPLIER_COMMERCIALS("Amend Supplier Commercials"),
    AMEND_SELLING_PRICE("Amend Selling Price"),
    AMENDMENTS("Amendments"),
    REINSTATE_ORIGINAL_BOOKINGS("Reinstate Original Booking2"),
    REPRICING_DURING_TICKETING("Repricing During Ticketing"),

    SHARING_CONCEPT("Sharing Concept"),
    CLAIM_DISCOUNT("Claim Discount"),
    DISCOUNT_SUPPLIER_PRICE("Discount On Supplier Price"),
    SUPPLEMENT_SUPPLIER_PRICE("Supplement On Supplier Price");

    private String reason;

    ReasonForRequest(String reason) {
        this.reason=reason;
    }
    public static ReasonForRequest getReasonForRequest(String reason)
    {
        ReasonForRequest reasonForRequest=null;
        for(ReasonForRequest reasonForRequest1:ReasonForRequest.values()){
            if(reasonForRequest1.getReason().equalsIgnoreCase(reason)){
                reasonForRequest=reasonForRequest1;
                break;
            }
        }
        return reasonForRequest;
    }
    public String getReason(){
        return this.reason;
    }
}
