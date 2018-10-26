package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.InboundPriority;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="inbound_recipient_details")
public class InboundRecipientDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="inbound_recipient_id")
    private String id;
    private String empName;
    private String empId;
    private String department;
    private String landLineNumber;
    private String extension;
    private String emailId;
    private String companyName;
    private String officeAddress;
    private String costCenter;
    @Enumerated
    private InboundPriority priority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLandLineNumber() {
        return landLineNumber;
    }

    public void setLandLineNumber(String landLineNumber) {
        this.landLineNumber = landLineNumber;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public InboundPriority getPriority() {
        return priority;
    }

    public void setPriority(InboundPriority priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InboundRecipientDetails that = (InboundRecipientDetails) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(empName, that.empName) &&
                Objects.equals(empId, that.empId) &&
                Objects.equals(department, that.department) &&
                Objects.equals(landLineNumber, that.landLineNumber) &&
                Objects.equals(extension, that.extension) &&
                Objects.equals(emailId, that.emailId) &&
                Objects.equals(companyName, that.companyName) &&
                Objects.equals(officeAddress, that.officeAddress) &&
                Objects.equals(costCenter, that.costCenter) &&
                Objects.equals(priority, that.priority);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, empName, empId, department, landLineNumber, extension, emailId, companyName, officeAddress, costCenter, priority);
    }

    @Override
    public String toString() {
        return "InboundRecipientDetails{" +
                "id='" + id + '\'' +
                ", empName='" + empName + '\'' +
                ", empId='" + empId + '\'' +
                ", department='" + department + '\'' +
                ", landLineNumber='" + landLineNumber + '\'' +
                ", extension='" + extension + '\'' +
                ", emailId='" + emailId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", officeAddress='" + officeAddress + '\'' +
                ", costCenter='" + costCenter + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
