package com.coxandkings.travel.operations.resource.notification;

public class ScheduleResource {
    private Boolean enableRecurringNotification;
    private WeeklyResource weekly;
    private String repeatMode;
    private Integer countValue;
    private String mode;
    private YearlyResource yearly;
    private MonthlyResource monthly;
    private DailyResource daily;
    private HourlyResource hourly;

    public Boolean getEnableRecurringNotification() {
        return enableRecurringNotification;
    }

    public void setEnableRecurringNotification(Boolean enableRecurringNotification) {
        this.enableRecurringNotification = enableRecurringNotification;
    }

    public WeeklyResource getWeekly() {
        return weekly;
    }

    public void setWeekly(WeeklyResource weekly) {
        this.weekly = weekly;
    }

    public String getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(String repeatMode) {
        this.repeatMode = repeatMode;
    }

    public Integer getCountValue() {
        return countValue;
    }

    public void setCountValue(Integer countValue) {
        this.countValue = countValue;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public YearlyResource getYearly() {
        return yearly;
    }

    public void setYearly(YearlyResource yearly) {
        this.yearly = yearly;
    }

    public MonthlyResource getMonthly() {
        return monthly;
    }

    public void setMonthly(MonthlyResource monthly) {
        this.monthly = monthly;
    }

    public DailyResource getDaily() {
        return daily;
    }

    public void setDaily(DailyResource daily) {
        this.daily = daily;
    }

    public HourlyResource getHourly() {
        return hourly;
    }

    public void setHourly(HourlyResource hourly) {
        this.hourly = hourly;
    }
}
