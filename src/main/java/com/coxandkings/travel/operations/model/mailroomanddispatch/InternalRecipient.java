package com.coxandkings.travel.operations.model.mailroomanddispatch;

import javax.persistence.Entity;

@Entity
public class InternalRecipient extends BaseRecipientDetails {
    private String employeeName;
    private String employeeId;
    private String companyName;
    private String division;
    private String department;
    private String mailroomName;
    private String floor;
    private String extensionNo;




    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMailroomName() {
        return mailroomName;
    }

    public void setMailroomName(String mailroomName) {
        this.mailroomName = mailroomName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getExtensionNo() {
        return extensionNo;
    }

    public void setExtensionNo(String extensionNo) {
        this.extensionNo = extensionNo;
    }
}
