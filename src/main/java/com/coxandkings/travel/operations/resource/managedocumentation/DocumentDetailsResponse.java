package com.coxandkings.travel.operations.resource.managedocumentation;

import java.util.List;

public class DocumentDetailsResponse {

    private List<BookingDocuments> bookingDocuments;
    private List<OrderDocuments> orderDocuments;
    private List<RoomDocuments> roomDocument;
    private List<PaxDocuments> paxDocument;

    public List<BookingDocuments> getBookingDocuments() {
        return bookingDocuments;
    }

    public void setBookingDocuments(List<BookingDocuments> bookingDocuments) {
        this.bookingDocuments = bookingDocuments;
    }

    public List<OrderDocuments> getOrderDocuments() {
        return orderDocuments;
    }

    public void setOrderDocuments(List<OrderDocuments> orderDocuments) {
        this.orderDocuments = orderDocuments;
    }

    public List<RoomDocuments> getRoomDocument() {
        return roomDocument;
    }

    public void setRoomDocument(List<RoomDocuments> roomDocument) {
        this.roomDocument = roomDocument;
    }

    public List<PaxDocuments> getPaxDocument() {
        return paxDocument;
    }

    public void setPaxDocument(List<PaxDocuments> paxDocument) {
        this.paxDocument = paxDocument;
    }
}
