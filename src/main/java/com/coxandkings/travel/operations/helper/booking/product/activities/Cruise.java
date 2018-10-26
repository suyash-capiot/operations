package com.coxandkings.travel.operations.helper.booking.product.activities;

public class Cruise extends Activity {
    private String cruisePackageName;
    private String shipName;
    private String destination;
    private Integer numberOfDays;
    private Integer numberOfNights;
    private String itinerary;
    private String cabinType;
    private String cabinCategory;
    private String deckNumber;

    public String getCruisePackageName() {
        return cruisePackageName;
    }

    public void setCruisePackageName(String cruisePackageName) {
        this.cruisePackageName = cruisePackageName;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public Integer getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(Integer numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public String getItinerary() {
        return itinerary;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }

    public String getCabinType() {
        return cabinType;
    }

    public void setCabinType(String cabinType) {
        this.cabinType = cabinType;
    }

    public String getCabinCategory() {
        return cabinCategory;
    }

    public void setCabinCategory(String cabinCategory) {
        this.cabinCategory = cabinCategory;
    }

    public String getDeckNumber() {
        return deckNumber;
    }

    public void setDeckNumber(String deckNumber) {
        this.deckNumber = deckNumber;
    }
}
