package com.coxandkings.travel.operations.resource.scheduler;

public class FortnightSettlementScheduleResponse  {

    private SettlementScheduleResponse firstFortnightScheduleResponse;

    private SettlementScheduleResponse secondFortnightScheduleResponse;

    public FortnightSettlementScheduleResponse() {}

    public SettlementScheduleResponse getFirstFortnightScheduleResponse() {
        return firstFortnightScheduleResponse;
    }

    public void setFirstFortnightScheduleResponse(SettlementScheduleResponse firstFortnightScheduleResponse) {
        this.firstFortnightScheduleResponse = firstFortnightScheduleResponse;
    }

    public SettlementScheduleResponse getSecondFortnightScheduleResponse() {
        return secondFortnightScheduleResponse;
    }

    public void setSecondFortnightScheduleResponse(SettlementScheduleResponse secondFortnightScheduleResponse) {
        this.secondFortnightScheduleResponse = secondFortnightScheduleResponse;
    }
}
