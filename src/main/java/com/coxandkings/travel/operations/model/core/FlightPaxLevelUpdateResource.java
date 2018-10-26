package com.coxandkings.travel.operations.model.core;

public class FlightPaxLevelUpdateResource {

    private String passengerID;
    private String status;
    private String seatNumber;
    private String ticketNumber;

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Override
    public String toString() {
        return "FlightPaxLevelUpdateResource{" +
                "passengerID='" + passengerID + '\'' +
                ", status='" + status + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", ticketNumber='" + ticketNumber + '\'' +
                '}';
    }
}
