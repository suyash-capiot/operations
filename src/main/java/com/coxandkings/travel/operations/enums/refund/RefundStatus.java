package com.coxandkings.travel.operations.enums.refund;

public enum RefundStatus {
    PENDING_WITH_OPS("PENDING WITH OPS"),
    PENDING_WITH_FINANCE("PENDING WITH FINANCE"),
    PROCESSED("PROCESSED");
    private String status;
    RefundStatus(String status) {
        this.status=status;
    }

    public String getStatus() {
        return this.status;
    }
}
