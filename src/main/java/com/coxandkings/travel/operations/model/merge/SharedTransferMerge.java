package com.coxandkings.travel.operations.model.merge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "shared_transfer_merge")
public class SharedTransferMerge extends Merge {

    @Column(name = "is_air_conditioned")
    private Boolean airCondition;

    @Column(name = "pick_up_location")
    private String pickupLocation;

    @Column(name = "travel_date")
    private Long travelDate;

    @Column(name = "pick_up_time")
    private Long pickUpTime;

    public SharedTransferMerge() {
    }

    public SharedTransferMerge(SharedTransferMerge sharedTransferMerge) {
        this.airCondition = sharedTransferMerge.getAirCondition();
        this.pickupLocation = sharedTransferMerge.getPickupLocation();
        this.travelDate = sharedTransferMerge.getTravelDate();
        this.pickUpTime = sharedTransferMerge.getPickUpTime();
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
