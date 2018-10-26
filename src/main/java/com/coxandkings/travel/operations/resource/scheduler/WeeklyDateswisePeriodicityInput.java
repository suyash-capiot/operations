package com.coxandkings.travel.operations.resource.scheduler;

import java.time.LocalDate;

public class WeeklyDateswisePeriodicityInput {

    private LocalDate fromDate;

    private LocalDate toDate;

    private LocalDate settlementDate;

    public WeeklyDateswisePeriodicityInput() {
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }
}
