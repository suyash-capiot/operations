package com.coxandkings.travel.operations.resource.scheduler;

import java.time.LocalDate;

public class QuarterlyPeriodicity {

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate settlementDueDate;

    public QuarterlyPeriodicity() {}

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getSettlementDueDate() {
        return settlementDueDate;
    }

    public void setSettlementDueDate(LocalDate settlementDueDate) {
        this.settlementDueDate = settlementDueDate;
    }
}
