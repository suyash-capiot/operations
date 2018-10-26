package com.coxandkings.travel.operations.model.fullCancellation;

public class CancelBooking {
    private String bookId;
    private String orderNo;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
