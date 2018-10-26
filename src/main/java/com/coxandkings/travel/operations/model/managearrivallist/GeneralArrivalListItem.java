package com.coxandkings.travel.operations.model.managearrivallist;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Arrival_List_General")
public class GeneralArrivalListItem extends BaseModel
{

    @Column(name = "productCategory")
    private String productCategory;

    @Column(name = "productCategorySubType")
    private String productCategorySubType;

    @Column(name = "clientType")
    private String clientType;

    @Column(name = "clientGroup")
    private String clientGroup;

    @Column(name = "clientName")
    private String clientName;

    @Column(name = "supplierName")
    private String supplierName;


    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientGroup() {
        return clientGroup;
    }

    public void setClientGroup(String clientGroup) {
        this.clientGroup = clientGroup;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

 /*   public ArrivalListInfo getArrivalListInfo() {
        return arrivalListInfo;
    }

    public void setArrivalListInfo(ArrivalListInfo arrivalListInfo) {
        this.arrivalListInfo = arrivalListInfo;
    }*/
}

