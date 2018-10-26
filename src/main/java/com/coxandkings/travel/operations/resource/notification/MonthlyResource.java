package com.coxandkings.travel.operations.resource.notification;

public class MonthlyResource {

    private String monthForWeekWiseSchedule;
    private String dayOfMonthForDayWiseSchedule;
    private StartTime startTime;

    public String getMonthForWeekWiseSchedule() {
        return monthForWeekWiseSchedule;
    }

    public void setMonthForWeekWiseSchedule(String monthForWeekWiseSchedule) {
        this.monthForWeekWiseSchedule = monthForWeekWiseSchedule;
    }

    public String getDayOfMonthForDayWiseSchedule() {
        return dayOfMonthForDayWiseSchedule;
    }

    public void setDayOfMonthForDayWiseSchedule(String dayOfMonthForDayWiseSchedule) {
        this.dayOfMonthForDayWiseSchedule = dayOfMonthForDayWiseSchedule;
    }

    public StartTime getStartTime() {
        return startTime;
    }

    public void setStartTime(StartTime startTime) {
        this.startTime = startTime;
    }
}
