package com.coxandkings.travel.operations.resource.booking;

import com.coxandkings.travel.operations.model.core.OpsOrderStatus;

public class UpdateAccommodationDetailResource {

    private String bookId;
    private String orderId;
    private String roomId;
    private String checkInDate;
    private String checkOutDate;
    private OpsOrderStatus roomStatus;

    private String supplierRefNo;

    private String accomodationRefNo;

    public String getSupplierRefNo() {

        return supplierRefNo;
    }

    public void setSupplierRefNo(String supplierRefNo) {
        this.supplierRefNo = supplierRefNo;
    }

    public String getAccomodationRefNo() {
        return accomodationRefNo;
    }

    public void setAccomodationRefNo(String accomodationRefNo) {
        this.accomodationRefNo = accomodationRefNo;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public OpsOrderStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(OpsOrderStatus roomStatus) {
        this.roomStatus = roomStatus;
    }


}
