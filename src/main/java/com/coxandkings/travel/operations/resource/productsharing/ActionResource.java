package com.coxandkings.travel.operations.resource.productsharing;

public class ActionResource {

    private String serialNumberOfFirstPassenger;
    private String bookRefIDOfFirstPassenger;
    private String orderIDOfFirstPassenger;
    private String passengerIDOfFirstPassenger;
    private String comment;

    private ToShareResource toShare;

    public ToShareResource getToShare() {
        return toShare;
    }

    public void setToShare(ToShareResource toShare) {
        this.toShare = toShare;
    }

    public String getSerialNumberOfFirstPassenger() {
        return serialNumberOfFirstPassenger;
    }

    public void setSerialNumberOfFirstPassenger(String serialNumberOfFirstPassenger) {
        this.serialNumberOfFirstPassenger = serialNumberOfFirstPassenger;
    }

    public String getBookRefIDOfFirstPassenger() {
        return bookRefIDOfFirstPassenger;
    }

    public void setBookRefIDOfFirstPassenger(String bookRefIDOfFirstPassenger) {
        this.bookRefIDOfFirstPassenger = bookRefIDOfFirstPassenger;
    }

    public String getOrderIDOfFirstPassenger() {
        return orderIDOfFirstPassenger;
    }

    public void setOrderIDOfFirstPassenger(String orderIDOfFirstPassenger) {
        this.orderIDOfFirstPassenger = orderIDOfFirstPassenger;
    }

    public String getPassengerIDOfFirstPassenger() {
        return passengerIDOfFirstPassenger;
    }

    public void setPassengerIDOfFirstPassenger(String passengerIDOfFirstPassenger) {
        this.passengerIDOfFirstPassenger = passengerIDOfFirstPassenger;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ActionResource{" +
                "serialNumberOfFirstPassenger='" + serialNumberOfFirstPassenger + '\'' +
                ", bookRefIDOfFirstPassenger='" + bookRefIDOfFirstPassenger + '\'' +
                ", orderIDOfFirstPassenger='" + orderIDOfFirstPassenger + '\'' +
                ", passengerIDOfFirstPassenger='" + passengerIDOfFirstPassenger + '\'' +
                ", toShare=" + toShare +
                '}';
    }
}
