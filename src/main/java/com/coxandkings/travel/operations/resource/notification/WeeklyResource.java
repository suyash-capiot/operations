package com.coxandkings.travel.operations.resource.notification;

import java.util.List;

public class WeeklyResource {
    private List<String> daysOfWeek;
    private StartTime startTime;

    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public StartTime getStartTime() {
        return startTime;
    }

    public void setStartTime(StartTime startTime) {
        this.startTime = startTime;
    }
}
