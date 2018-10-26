package com.coxandkings.travel.operations.model.remarks;


import com.coxandkings.travel.operations.enums.remarks.RemarksCategory;
import com.coxandkings.travel.operations.enums.remarks.RemarksTo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "booking_remarks")
public class BookingRemarks {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String remarkId;
    private String bookId;
    @Enumerated(EnumType.STRING)
    private RemarksTo remarksTo;
    @Enumerated(EnumType.STRING)
    private RemarksCategory remarksCategory;
    @Column(name = "curr_user")
    private String currentUser;
    private String userRole;
    private String userId;
    private String userName;
    private String details;


    public BookingRemarks() {
    }

    public BookingRemarks(String remarkId, String bookId, RemarksTo remarksTo, RemarksCategory remarksCategory, String currentUser, String userRole, String userId, String userName, String details) {
        this.remarkId = remarkId;
        this.bookId = bookId;
        this.remarksTo = remarksTo;
        this.remarksCategory = remarksCategory;
        this.currentUser = currentUser;
        this.userRole = userRole;
        this.userId = userId;
        this.userName = userName;
        this.details = details;
    }

    public String getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(String remarkId) {
        this.remarkId = remarkId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public RemarksTo getRemarksTo() {
        return remarksTo;
    }

    public void setRemarksTo(RemarksTo remarksTo) {
        this.remarksTo = remarksTo;
    }

    public RemarksCategory getRemarksCategory() {
        return remarksCategory;
    }

    public void setRemarksCategory(RemarksCategory remarksCategory) {
        this.remarksCategory = remarksCategory;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


}
