package com.coxandkings.travel.operations.model.alternateoptions;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionFollowUpStatus;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionsReminderType;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionsSchedularType;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name="ALTERNATEOPTIONSFOLLOWUP")
public class AlternateOptionsFollowUpDetails {

  @Id
  @Column
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid",strategy = "uuid")
  private String id;
  
  @Column
  private String subject;
  
  @Column
  private String followUpActivityType;
  
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @Column(name = "creationDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime creationDate;

  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @Column(name = "dueDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime dueDate; //format yyyy-mm-dd
  
  @Column
  private String status;
  
  @Column
  private String assignedUserRole; // make auto suggest
  
  @Column
  private String assignedUserName;
  
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @Column(name = "startDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime startDate;
  
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @Column(name = "startTime", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime startTime;
  
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @Column(name = "endTime", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime endTime;
  
  @Column
  private String scheduleType;
  
  @Column
  private String reminderType;
  
  @OneToOne(mappedBy = "alternateOptionsFollowUpDetails", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private AlternateOptionsReOccurrence reOccurringSchedule;
  
  @Column
  @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.lang.String")})
  private String alternateOptionsResponseDetails;
  
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

  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(ZonedDateTime creationDate) {
    this.creationDate = creationDate;
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

  public AlternateOptionsReOccurrence getReOccurringSchedule() {
    return reOccurringSchedule;
  }

  public void setReOccurringSchedule(AlternateOptionsReOccurrence reOccurringSchedule) {
    this.reOccurringSchedule = reOccurringSchedule;
  }

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

  public String getAlternateOptionsResponseDetails() {
    return alternateOptionsResponseDetails;
  }

  public void setAlternateOptionsResponseDetails(String alternateOptionsResponseDetails) {
    this.alternateOptionsResponseDetails = alternateOptionsResponseDetails;
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
