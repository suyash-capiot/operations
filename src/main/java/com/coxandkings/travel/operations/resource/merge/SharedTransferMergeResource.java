package com.coxandkings.travel.operations.resource.merge;

public class SharedTransferMergeResource {
    private Boolean airCondition;
    private String pickupLocation;
    private Long travelDate;
    private Long pickUpTime;

    public SharedTransferMergeResource() {
    }

    public Boolean getAirCondition() {
        return airCondition;
    }

    public void setAirCondition(Boolean airCondition) {
        this.airCondition = airCondition;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Long getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Long travelDate) {
        this.travelDate = travelDate;
    }

    public Long getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(Long pickUpTime) {
        this.pickUpTime = pickUpTime;
    }
}
