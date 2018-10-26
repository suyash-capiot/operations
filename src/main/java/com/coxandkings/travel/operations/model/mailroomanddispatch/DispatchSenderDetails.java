package com.coxandkings.travel.operations.model.mailroomanddispatch;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="dispatch_sender_details")
public class DispatchSenderDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="dispatch_sender_id")
    private String senderId;
    private String employeeId;
    private String employeeName;
    private String companyName;
    private String department;
    private String division;
    private String mailRoomName;
    private String country;
    private String city;
    private String postalCode;
    private String landLineNo;
    private String extensionNo;
    private String mobileNo;
    private String emailId;
    private String remarks;
    private String modeOfDispatch;
    private String supplierName;
    private String costCenter;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getMailRoomName() {
        return mailRoomName;
    }

    public void setMailRoomName(String mailRoomName) {
        this.mailRoomName = mailRoomName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLandLineNo() {
        return landLineNo;
    }

    public void setLandLineNo(String landLineNo) {
        this.landLineNo = landLineNo;
    }

    public String getExtensionNo() {
        return extensionNo;
    }

    public void setExtensionNo(String extensionNo) {
        this.extensionNo = extensionNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getModeOfDispatch() {
        return modeOfDispatch;
    }

    public void setModeOfDispatch(String modeOfDispatch) {
        this.modeOfDispatch = modeOfDispatch;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DispatchSenderDetails that = (DispatchSenderDetails) o;
        return Objects.equals(senderId, that.senderId) &&
                Objects.equals(employeeId, that.employeeId) &&
                Objects.equals(employeeName, that.employeeName) &&
                Objects.equals(companyName, that.companyName) &&
                Objects.equals(department, that.department) &&
                Objects.equals(division, that.division) &&
                Objects.equals(mailRoomName, that.mailRoomName) &&
                Objects.equals(country, that.country) &&
                Objects.equals(city, that.city) &&
                Objects.equals(postalCode, that.postalCode) &&
                Objects.equals(landLineNo, that.landLineNo) &&
                Objects.equals(extensionNo, that.extensionNo) &&
                Objects.equals(mobileNo, that.mobileNo) &&
                Objects.equals(emailId, that.emailId) &&
                Objects.equals(remarks, that.remarks) &&
                Objects.equals(modeOfDispatch, that.modeOfDispatch) &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(costCenter, that.costCenter);

    }

    @Override
    public int hashCode() {

        return Objects.hash(senderId, employeeId, employeeName, companyName, department, division, mailRoomName, country, city, postalCode, landLineNo, extensionNo, mobileNo, emailId, remarks, modeOfDispatch, supplierName, costCenter);
    }

    @Override
    public String toString() {
        return "DispatchSenderDetails{" +
                "senderId='" + senderId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", department='" + department + '\'' +
                ", division='" + division + '\'' +
                ", mailRoomName='" + mailRoomName + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", landLineNumber=" + landLineNo +
                ", extensionNumber='" + extensionNo + '\'' +
                ", mobileNumber=" + mobileNo +
                ", emailId='" + emailId + '\'' +
                ", remarks='" + remarks + '\'' +
                ", modeOfDispatch='" + modeOfDispatch + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", costCentre='" + costCenter + '\'' +
                //", dispatchCostDetails=" + dispatchCostDetails +
                '}';
    }
}
