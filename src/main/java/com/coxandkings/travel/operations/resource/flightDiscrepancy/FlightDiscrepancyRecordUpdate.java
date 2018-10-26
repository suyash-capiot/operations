package com.coxandkings.travel.operations.resource.flightDiscrepancy;

public class FlightDiscrepancyRecordUpdate {

    private String id;
    private String discrepancyStatus;
    private String bspDisputeId;
    private String discrepancyRecordId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiscrepancyStatus() {
        return discrepancyStatus;
    }

    public void setDiscrepancyStatus(String discrepancyStatus) {
        this.discrepancyStatus = discrepancyStatus;
    }

    public String getBspDisputeId() {
        return bspDisputeId;
    }

    public void setBspDisputeId(String bspDisputeId) {
        this.bspDisputeId = bspDisputeId;
    }

    public String getDiscrepancyRecordId() {
        return discrepancyRecordId;
    }

    public void setDiscrepancyRecordId(String discrepancyRecordId) {
        this.discrepancyRecordId = discrepancyRecordId;
    }
}
