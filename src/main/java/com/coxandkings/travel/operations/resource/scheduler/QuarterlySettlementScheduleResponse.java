package com.coxandkings.travel.operations.resource.scheduler;

public class QuarterlySettlementScheduleResponse {

    private SettlementScheduleResponse q1Response;

    private SettlementScheduleResponse q2Response;

    private SettlementScheduleResponse q3Response;

    private SettlementScheduleResponse q4Response;

    public QuarterlySettlementScheduleResponse() {};

    public SettlementScheduleResponse getQ1Response() {
        return q1Response;
    }

    public void setQ1Response(SettlementScheduleResponse q1Response) {
        this.q1Response = q1Response;
    }

    public SettlementScheduleResponse getQ2Response() {
        return q2Response;
    }

    public void setQ2Response(SettlementScheduleResponse q2Response) {
        this.q2Response = q2Response;
    }

    public SettlementScheduleResponse getQ3Response() {
        return q3Response;
    }

    public void setQ3Response(SettlementScheduleResponse q3Response) {
        this.q3Response = q3Response;
    }

    public SettlementScheduleResponse getQ4Response() {
        return q4Response;
    }

    public void setQ4Response(SettlementScheduleResponse q4Response) {
        this.q4Response = q4Response;
    }
}
