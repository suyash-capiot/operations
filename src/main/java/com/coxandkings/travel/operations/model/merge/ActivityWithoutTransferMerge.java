package com.coxandkings.travel.operations.model.merge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "activity_without_shared_transfer_merge")
public class ActivityWithoutTransferMerge extends Merge {

    //TODO : add the attributes here
    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_name_sub_type")
    private String productNameSubType;

    @Column(name = "travel_date")
    private Long travelDate;

    public ActivityWithoutTransferMerge() {
    }

    public ActivityWithoutTransferMerge(ActivityWithoutTransferMerge activityWithoutTransferMerge) {
//        this.id = activityWithoutTransferMerge.getId();
        this.productName = activityWithoutTransferMerge.getProductName();
        this.productNameSubType = activityWithoutTransferMerge.getProductNameSubType();
        this.travelDate = activityWithoutTransferMerge.getTravelDate();
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
}
