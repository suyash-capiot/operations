package com.coxandkings.travel.operations.enums.flightDiscrepancy;

public enum DiscrepancyStatus {
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    DISPUTE_RAISED("Dispute_Raised");
    private String value;

    DiscrepancyStatus(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
