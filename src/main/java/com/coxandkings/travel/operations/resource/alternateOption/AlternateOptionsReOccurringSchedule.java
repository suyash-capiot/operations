package com.coxandkings.travel.operations.resource.alternateOption;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AlternateOptionsReOccurringSchedule {
  
  private String id;
  
  private String reOccurrenceType;
  
  //Daily Occurrence
  private Boolean isWeekday;
  
  private Integer afterHowManyDays;
  
  //Weekly Occurrence
  private String dayOfTheWeek;
  
  private Integer numberOfWeeks;
  
  //Monthly Occurrence
  private Boolean isWeekdayMonthlyOccurrence;
  
  private Integer day;
  
  private Integer month;
  
  private String week;
  
  //End On Occurrence
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private ZonedDateTime endDate;
  
  private Long numberOfOccurrences;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Boolean getIsWeekday() {
    return isWeekday;
  }

  public void setIsWeekday(Boolean isWeekday) {
    this.isWeekday = isWeekday;
  }

  public Integer getAfterHowManyDays() {
    return afterHowManyDays;
  }

  public void setAfterHowManyDays(Integer afterHowManyDays) {
    this.afterHowManyDays = afterHowManyDays;
  }

  public String getDayOfTheWeek() {
    return dayOfTheWeek;
  }

  public void setDayOfTheWeek(String dayOfTheWeek) {
    this.dayOfTheWeek = dayOfTheWeek;
  }

  public Integer getNumberOfWeeks() {
    return numberOfWeeks;
  }

  public void setNumberOfWeeks(Integer numberOfWeeks) {
    this.numberOfWeeks = numberOfWeeks;
  }

  public Integer getDay() {
    return day;
  }

  public void setDay(Integer day) {
    this.day = day;
  }

  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public String getWeek() {
    return week;
  }

  public void setWeek(String week) {
    this.week = week;
  }

  public Long getNumberOfOccurrences() {
    return numberOfOccurrences;
  }

  public void setNumberOfOccurrences(Long numberOfOccurrences) {
    this.numberOfOccurrences = numberOfOccurrences;
  }

  public ZonedDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(ZonedDateTime endDate) {
    this.endDate = endDate;
  }

  public String getReOccurrenceType() {
    return reOccurrenceType;
  }

  public void setReOccurrenceType(String reOccurrenceType) {
    this.reOccurrenceType = reOccurrenceType;
  }

  public Boolean getIsWeekdayMonthlyOccurrence() {
    return isWeekdayMonthlyOccurrence;
  }

  public void setIsWeekdayMonthlyOccurrence(Boolean isWeekdayMonthlyOccurrence) {
    this.isWeekdayMonthlyOccurrence = isWeekdayMonthlyOccurrence;
  }
  
  

  

}
