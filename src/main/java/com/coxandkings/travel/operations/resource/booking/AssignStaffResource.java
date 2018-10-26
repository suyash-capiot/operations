package com.coxandkings.travel.operations.resource.booking;

import java.util.List;

public class AssignStaffResource {
    private String userID;
    private String staffID;
    private String staffName;
    private List<String> bookIDs;
    private String companyID;

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public List<String> getBookIDs() {
        return bookIDs;
    }

    public void setBookIDs(List<String> bookIDs) {
        this.bookIDs = bookIDs;
    }
}
