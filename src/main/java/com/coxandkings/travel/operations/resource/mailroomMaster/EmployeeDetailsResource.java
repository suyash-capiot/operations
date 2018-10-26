package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.EmpStatus;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class EmployeeDetailsResource {
    private String mailRoomId;
    private String employeeId;
    private String employeeName;
    private String employeeEmailId;
    private String employeePhoneNo;
    private EmpStatus employeeStatus;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime employeeActivityExpiryDate;

    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime employeeActivityStartDate;

    public EmpStatus getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(EmpStatus employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getMailRoomId() {
        return mailRoomId;
    }

    public void setMailRoomId(String mailRoomId) {
        this.mailRoomId = mailRoomId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeEmailId() {
        return employeeEmailId;
    }

    public void setEmployeeEmailId(String employeeEmailId) {
        this.employeeEmailId = employeeEmailId;
    }

    public String getEmployeePhoneNo() {
        return employeePhoneNo;
    }

    public void setEmployeePhoneNo(String employeePhoneNo) {
        this.employeePhoneNo = employeePhoneNo;
    }

    public ZonedDateTime getEmployeeActivityExpiryDate() {
        return employeeActivityExpiryDate;
    }

    public void setEmployeeActivityExpiryDate(ZonedDateTime employeeActivityExpiryDate) {
        this.employeeActivityExpiryDate = employeeActivityExpiryDate;
    }

    public ZonedDateTime getEmployeeActivityStartDate() {
        return employeeActivityStartDate;
    }

    public void setEmployeeActivityStartDate(ZonedDateTime employeeActivityStartDate) {
        this.employeeActivityStartDate = employeeActivityStartDate;
    }
}

