package com.coxandkings.travel.operations.model.timelimitbooking;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "revised_timelimit_info")
public class TimeLimitExpiryInfo {

    @Id
    private String bookID;
    private String orderID;
    private String newDate;
    private String userId;

    public TimeLimitExpiryInfo() {
    }

    public TimeLimitExpiryInfo(String bookID, String orderID, String newDate, String userId) {
        this.bookID = bookID;
        this.orderID = orderID;
        this.newDate = newDate;
        this.userId = userId;
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

    public String getNewDate() {
        return newDate;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
