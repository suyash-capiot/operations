package com.coxandkings.travel.operations.model.alternateoptions;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name="ALTERNATEOPTIONSREOCCURRENCE")
public class AlternateOptionsReOccurrence {

  @Id
  @Column
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid",strategy = "uuid")
  private String id;
  
  @Column
  private String reOccurrenceType;
  
  //Daily Occurrence
  @Column
  private Boolean isWeekday;
  
  @Column
  private Integer afterHowManyDays;
  
  //Weekly Occurrence
  @Column
  private String dayOfTheWeek;
  
  @Column
  private Integer numberOfWeeks;
  
  //Monthly Occurrence
  @Column
  private Boolean isWeekdayMonthlyOccurrence;
  
  @Column
  private Integer day;
  
  @Column
  private Integer month;
  
  @Column
  private String week;
  
  //End On Occurrence
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @Column(name = "endDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime endDate;
  
  @Column
  private Long numberOfOccurrences;
  
  @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonBackReference
  private AlternateOptionsFollowUpDetails alternateOptionsFollowUpDetails;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public AlternateOptionsFollowUpDetails getAlternateOptionsFollowUpDetails() {
    return alternateOptionsFollowUpDetails;
  }

  public void setAlternateOptionsFollowUpDetails(
      AlternateOptionsFollowUpDetails alternateOptionsFollowUpDetails) {
    this.alternateOptionsFollowUpDetails = alternateOptionsFollowUpDetails;
  }

  public Boolean getIsWeekday() {
    return isWeekday;
  }

  public void setIsWeekday(Boolean isWeekday) {
    this.isWeekday = isWeekday;
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
