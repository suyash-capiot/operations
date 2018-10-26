package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.EmpStatus;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "mailroom_employee_details")
public class EmployeeDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "employee_details_id")
    private String id;
    private String employeeId;
    private String employeeName;
    private String employeeEmailId;
    private String employeePhoneNo;
    private EmpStatus empStatus;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime employeeActivityExpiryDate;

    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime employeeActivityStartDate;

    public JSONObject toEmpNameIdJson(){
        JSONObject empNameIdJson = new JSONObject();

        empNameIdJson.put("employeeName",employeeName);
        empNameIdJson.put("employeeID",employeeId);

        return empNameIdJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public EmpStatus getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(EmpStatus empStatus) {
        this.empStatus = empStatus;
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
