package com.coxandkings.travel.operations.resource.scheduler;

public class HalfYearlySchedulerResponse {
    private SettlementScheduleResponse firstHalfYearlySchedulerResponse;
    private SettlementScheduleResponse secondHalfYearlySchedulerResponse;

    public SettlementScheduleResponse getFirstHalfYearlySchedulerResponse() {
        return firstHalfYearlySchedulerResponse;
    }

    public void setFirstHalfYearlySchedulerResponse(SettlementScheduleResponse firstHalfYearlySchedulerResponse) {
        this.firstHalfYearlySchedulerResponse = firstHalfYearlySchedulerResponse;
    }

    public SettlementScheduleResponse getSecondHalfYearlySchedulerResponse() {
        return secondHalfYearlySchedulerResponse;
    }

    public void setSecondHalfYearlySchedulerResponse(SettlementScheduleResponse secondHalfYearlySchedulerResponse) {
        this.secondHalfYearlySchedulerResponse = secondHalfYearlySchedulerResponse;
    }
}
