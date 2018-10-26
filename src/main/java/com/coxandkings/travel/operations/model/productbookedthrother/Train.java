package com.coxandkings.travel.operations.model.productbookedthrother;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "other_product_info_train")
public class Train extends CommanAttribute
{

    private String trainName;
    private String trainNo;
    private String platformNo;

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

}