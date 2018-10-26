package com.coxandkings.travel.operations.resource.notification;

public class HourlyResource {

    private String everyHourDuration;
    private StartTime startTime;

    public String getEveryHourDuration() {
        return everyHourDuration;
    }

    public void setEveryHourDuration(String everyHourDuration) {
        this.everyHourDuration = everyHourDuration;
    }

    public StartTime getStartTime() {
        return startTime;
    }

    public void setStartTime(StartTime startTime) {
        this.startTime = startTime;
    }
}
