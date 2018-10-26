package com.coxandkings.travel.operations.enums.timelimit;

public enum  ApprovalStatus {
    Accepted("accepted"),
    Rejected("rejected"),
    Pending("pending");
    private String status;

    ApprovalStatus(String status) {
        this.status = status;

    }

    public String getStatus() {
        return this.name();
    }
}
