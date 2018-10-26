package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.resource.BaseResource;

public class BarCodeResource  extends BaseResource {
    private String id;
    private String consigneeName;
    private String consigneeLocation;
    private String companyName;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeLocation() {
        return consigneeLocation;
    }

    public void setConsigneeLocation(String consigneeLocation) {
        this.consigneeLocation = consigneeLocation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "BarCodeResource{" +
                "id='" + id + '\'' +
                ", consigneeName='" + consigneeName + '\'' +
                ", consigneeLocation='" + consigneeLocation + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
