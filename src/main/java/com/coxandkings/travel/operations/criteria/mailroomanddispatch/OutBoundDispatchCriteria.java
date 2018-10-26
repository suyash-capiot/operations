package com.coxandkings.travel.operations.criteria.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.BaseCriteria;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchStatus;

public class OutBoundDispatchCriteria extends BaseCriteria {

    private String id;
    private String parcelId;
    private String passportNumber;
    private String employeeName;
    private String employeeId;
    private String passengerName;
    private String supplierName;
    private DispatchStatus dispatchStatus;
    private Integer pageSize;
    private Integer pageNumber;
    private String referenceNumber;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public DispatchStatus getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(DispatchStatus dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

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

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Integer getPageNumber() {
        return pageNumber;
    }

    @Override
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
