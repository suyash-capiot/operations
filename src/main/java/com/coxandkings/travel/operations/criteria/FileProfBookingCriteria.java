package com.coxandkings.travel.operations.criteria;

public class FileProfBookingCriteria {
    private String id;
    private String orderId;
    private String passengerName;
    private String bookingReferenceNumber;
    private boolean isPassengerwise;
    private boolean isRoomwise;
    private boolean isTranspotation;
    private boolean isAccomodation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public boolean isPassengerwise() {
        return isPassengerwise;
    }

    public void setPassengerwise(boolean passengerwise) {
        isPassengerwise = passengerwise;
    }

    public boolean isRoomwise() {
        return isRoomwise;
    }

    public void setRoomwise(boolean roomwise) {
        isRoomwise = roomwise;
    }

    public boolean isTranspotation() {
        return isTranspotation;
    }

    public void setTranspotation(boolean transpotation) {
        isTranspotation = transpotation;
    }

    public boolean isAccomodation() {
        return isAccomodation;
    }

    public void setAccomodation(boolean accomodation) {
        isAccomodation = accomodation;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }
}
