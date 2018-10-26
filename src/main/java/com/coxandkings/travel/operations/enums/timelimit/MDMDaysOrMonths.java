package com.coxandkings.travel.operations.enums.timelimit;

public enum MDMDaysOrMonths {

    DAYS("days"),
    MONTHS("months");
    public String type;

    MDMDaysOrMonths(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
