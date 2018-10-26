package com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class DuplicateBookings {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;
    @ElementCollection
    private List<String> duplicateOrderBookingIds;
    private String bookingRefNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getDuplicateOrderBookingIds() {
        return duplicateOrderBookingIds;
    }

    public void setDuplicateOrderBookingIds(List<String> duplicateOrderBookingIds) {
        this.duplicateOrderBookingIds = duplicateOrderBookingIds;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }
}
