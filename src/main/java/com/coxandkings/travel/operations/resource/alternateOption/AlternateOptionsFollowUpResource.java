package com.coxandkings.travel.operations.resource.alternateOption;

import java.time.ZonedDateTime;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionFollowUpStatus;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionsReminderType;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionsSchedularType;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlternateOptionsFollowUpResource {

  private String bookID;
  
  private String orderID;
  
  private String id;
  
  private String subject;
  
  private String followUpActivityType;
  
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private ZonedDateTime dueDate; 
  
  private String status;
  
  private String assignedUserRole; // make auto suggest
  
  private String assignedUserName;

  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private ZonedDateTime startDate;
  
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private ZonedDateTime startTime;
  
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private ZonedDateTime endTime;
  
  private String scheduleType;
  
  private String reminderType;
  
  private AlternateOptionsReOccurringSchedule reOccurringSchedule;
  
  //private AlternateOptionsResponseDetailsResource alternateOptionsSent;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getFollowUpActivityType() {
    return followUpActivityType;
  }

  public void setFollowUpActivityType(String followUpActivityType) {
    this.followUpActivityType = followUpActivityType;
  }

  public String getAssignedUserRole() {
    return assignedUserRole;
  }

  public void setAssignedUserRole(String assignedUserRole) {
    this.assignedUserRole = assignedUserRole;
  }

  public String getAssignedUserName() {
    return assignedUserName;
  }

  public void setAssignedUserName(String assignedUserName) {
    this.assignedUserName = assignedUserName;
  }
  
  public AlternateOptionsReOccurringSchedule getReOccurringSchedule() {
    return reOccurringSchedule;
  }

  public void setReOccurringSchedule(AlternateOptionsReOccurringSchedule reOccurringSchedule) {
    this.reOccurringSchedule = reOccurringSchedule;
  }

  public String getBookID() {
    return bookID;
  }

  public void setBookID(String bookID) {
    this.bookID = bookID;
  }

  public String getOrderID() {
    return orderID;
  }

  public void setOrderID(String orderID) {
    this.orderID = orderID;
  }

  /*public AlternateOptionsResponseDetailsResource getAlternateOptionsSent() {
    return alternateOptionsSent;
  }

  public void setAlternateOptionsSent(AlternateOptionsResponseDetailsResource alternateOptionsSent) {
    this.alternateOptionsSent = alternateOptionsSent;
  }*/

  public ZonedDateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(ZonedDateTime dueDate) {
    this.dueDate = dueDate;
  }

  public ZonedDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(ZonedDateTime startDate) {
    this.startDate = startDate;
  }

  public ZonedDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(ZonedDateTime startTime) {
    this.startTime = startTime;
  }

  public ZonedDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(ZonedDateTime endTime) {
    this.endTime = endTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getScheduleType() {
    return scheduleType;
  }

  public void setScheduleType(String scheduleType) {
    this.scheduleType = scheduleType;
  }

  public String getReminderType() {
    return reminderType;
  }

  public void setReminderType(String reminderType) {
    this.reminderType = reminderType;
  }
  
  
  
}
