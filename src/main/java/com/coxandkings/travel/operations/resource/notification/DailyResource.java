package com.coxandkings.travel.operations.resource.notification;

public class DailyResource {

    private String everyDaysDuration;
    private String everyOrWeekDays;
    private StartTime startTime;

    public String getEveryDaysDuration() {
        return everyDaysDuration;
    }

    public void setEveryDaysDuration(String everyDaysDuration) {
        this.everyDaysDuration = everyDaysDuration;
    }

    public String getEveryOrWeekDays() {
        return everyOrWeekDays;
    }

    public void setEveryOrWeekDays(String everyOrWeekDays) {
        this.everyOrWeekDays = everyOrWeekDays;
    }

    public StartTime getStartTime() {
        return startTime;
    }

    public void setStartTime(StartTime startTime) {
        this.startTime = startTime;
    }
}
