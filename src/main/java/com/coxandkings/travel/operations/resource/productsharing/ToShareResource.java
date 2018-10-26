package com.coxandkings.travel.operations.resource.productsharing;

public class ToShareResource {

    private String serialNumber;
    private String bookRefID;
    private String orderID;
    private String passengerID;
    private ProductSharingStatus status;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBookRefID() {
        return bookRefID;
    }

    public void setBookRefID(String bookRefID) {
        this.bookRefID = bookRefID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public ProductSharingStatus getStatus() {
        return status;
    }

    public void setStatus(ProductSharingStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ToShareResource{" +
                "serialNumber='" + serialNumber + '\'' +
                ", bookRefID='" + bookRefID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", passengerID='" + passengerID + '\'' +
                ", status=" + status +
                '}';
    }
}
