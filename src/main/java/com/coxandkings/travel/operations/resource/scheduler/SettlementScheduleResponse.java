package com.coxandkings.travel.operations.resource.scheduler;

import java.time.LocalDate;

public class SettlementScheduleResponse {

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate settlementDueDay;

    private boolean shouldRunToday;

    public SettlementScheduleResponse()    {
    }

    public String toString()    {
        StringBuilder buffer = new StringBuilder();
        int fromYear = startDate.getYear();
        int fromMonth = startDate.getMonthValue();
        int fromDay = startDate.getDayOfMonth();

        int toYear = endDate.getYear();
        int toMonth = endDate.getMonthValue();
        int toDay = endDate.getDayOfMonth();

        int settlementYear = settlementDueDay.getYear();
        int settlementMonth = settlementDueDay.getMonthValue();
        int settlementDay = settlementDueDay.getDayOfMonth();

        buffer.append( "From date: " + fromDay + "-" + fromMonth + "-" + fromYear + "\n" );
        buffer.append( "To date: " + toDay + "-" + toMonth + "-" + toYear + "\n" );
        buffer.append( "Settlement date: " + settlementDay + "-" + settlementMonth + "-" + settlementYear + "\n");
        buffer.append( "Should run today:" + shouldRunToday + "\n" );

        return buffer.toString();
    }

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

    public LocalDate getSettlementDueDay() {
        return settlementDueDay;
    }

    public void setSettlementDueDay(LocalDate settlementDueDay) {
        this.settlementDueDay = settlementDueDay;
    }

    public boolean isShouldRunToday() {
        return shouldRunToday;
    }

    public void setShouldRunToday(boolean shouldRunToday) {
        this.shouldRunToday = shouldRunToday;
    }
}