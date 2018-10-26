package com.coxandkings.travel.operations.model.merge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "activity_with_shared_transfer_merge")
public class ActivityWithSharedTransferMerge extends Merge {

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_name_sub_type")
    private String productNameSubType;

    @Column(name = "travel_date")
    private Long travelDate;

    @Column(name = "is_air_conditioned")
    private Boolean airConditioned;

    @Column(name = "vehicle_category")
    private String vehicleCategory;

    public ActivityWithSharedTransferMerge() {
    }

    public ActivityWithSharedTransferMerge(ActivityWithSharedTransferMerge activityWithSharedTransferMerge) {
//        this.id = activityWithSharedTransferMerge.getId();
        this.productName = activityWithSharedTransferMerge.getProductName();
        this.productNameSubType = activityWithSharedTransferMerge.getProductNameSubType();
        this.travelDate = activityWithSharedTransferMerge.getTravelDate();
        this.airConditioned = activityWithSharedTransferMerge.getAirConditioned();
        this.vehicleCategory = activityWithSharedTransferMerge.getVehicleCategory();
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameSubType() {
        return productNameSubType;
    }

    public void setProductNameSubType(String productNameSubType) {
        this.productNameSubType = productNameSubType;
    }

    public Long getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Long travelDate) {
        this.travelDate = travelDate;
    }

    public Boolean getAirConditioned() {
        return airConditioned;
    }

    public void setAirConditioned(Boolean airConditioned) {
        this.airConditioned = airConditioned;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public void setVehicleCategory(String vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }
}
