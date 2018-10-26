package com.coxandkings.travel.operations.resource.scheduler;

public class FortnightlyPeriodicityInput {

    private int firstFortnightSettlementDueDay;

    // Use true if settlement due month is Current
    // Use false if settlement due month is Next month
    private boolean settleFirstFornightByCurrentMonth;

    private int secondFortnightSettlementDueDay;

    // Use true if settlement due month is Current
    // Use false if settlement due month is Next month
    private boolean settleSecondFornightByCurrentMonth;

    public FortnightlyPeriodicityInput()    {
    }

    public int getFirstFortnightSettlementDueDay() {
        return firstFortnightSettlementDueDay;
    }

    public void setFirstFortnightSettlementDueDay(int firstFortnightSettlementDueDay) {
        this.firstFortnightSettlementDueDay = firstFortnightSettlementDueDay;
    }

    public boolean isSettleFirstFornightByCurrentMonth() {
        return settleFirstFornightByCurrentMonth;
    }

    public void setSettleFirstFornightByCurrentMonth(boolean settleFirstFornightByCurrentMonth) {
        this.settleFirstFornightByCurrentMonth = settleFirstFornightByCurrentMonth;
    }

    public int getSecondFortnightSettlementDueDay() {
        return secondFortnightSettlementDueDay;
    }

    public void setSecondFortnightSettlementDueDay(int secondFortnightSettlementDueDay) {
        this.secondFortnightSettlementDueDay = secondFortnightSettlementDueDay;
    }

    public boolean isSettleSecondFornightByCurrentMonth() {
        return settleSecondFornightByCurrentMonth;
    }

    public void setSettleSecondFornightByCurrentMonth(boolean settleSecondFornightByCurrentMonth) {
        this.settleSecondFornightByCurrentMonth = settleSecondFornightByCurrentMonth;
    }
}

