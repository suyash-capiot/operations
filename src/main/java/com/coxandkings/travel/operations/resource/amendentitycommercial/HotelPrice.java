package com.coxandkings.travel.operations.resource.amendentitycommercial;

import com.coxandkings.travel.operations.model.core.*;

import java.util.List;

public class HotelPrice {

    private OpsAccommodationTotalPriceInfo orderTotalPriceInfo;
    private OpsAccoOrderSupplierPriceInfo orderSupplierPriceInfo;
    private OpsRoomTotalPriceInfo roomTotalPriceInfo;
    private OpsRoomSuppPriceInfo roomSupplierPriceInfo;
    private List<OpsClientEntityCommercial> roomClientCommercials;
    private List<OpsRoomSuppCommercial> roomSupplierCommercials;


    public List<OpsClientEntityCommercial> getRoomClientCommercials() {
        return roomClientCommercials;
    }

    public void setRoomClientCommercials(List<OpsClientEntityCommercial> roomClientCommercials) {
        this.roomClientCommercials = roomClientCommercials;
    }

    public OpsAccoOrderSupplierPriceInfo getOrderSupplierPriceInfo() {
        return orderSupplierPriceInfo;
    }

    public void setOrderSupplierPriceInfo(OpsAccoOrderSupplierPriceInfo orderSupplierPriceInfo) {
        this.orderSupplierPriceInfo = orderSupplierPriceInfo;
    }

    public OpsRoomSuppPriceInfo getRoomSupplierPriceInfo() {
        return roomSupplierPriceInfo;
    }

    public void setRoomSupplierPriceInfo(OpsRoomSuppPriceInfo roomSupplierPriceInfo) {
        this.roomSupplierPriceInfo = roomSupplierPriceInfo;
    }

    public List<OpsRoomSuppCommercial> getRoomSupplierCommercials() {
        return roomSupplierCommercials;
    }

    public void setRoomSupplierCommercials(List<OpsRoomSuppCommercial> roomSupplierCommercials) {
        this.roomSupplierCommercials = roomSupplierCommercials;
    }

    public OpsAccommodationTotalPriceInfo getOrderTotalPriceInfo() {
        return orderTotalPriceInfo;
    }

    public void setOrderTotalPriceInfo(OpsAccommodationTotalPriceInfo orderTotalPriceInfo) {
        this.orderTotalPriceInfo = orderTotalPriceInfo;
    }

    public OpsRoomTotalPriceInfo getRoomTotalPriceInfo() {
        return roomTotalPriceInfo;
    }

    public void setRoomTotalPriceInfo(OpsRoomTotalPriceInfo roomTotalPriceInfo) {
        this.roomTotalPriceInfo = roomTotalPriceInfo;
    }
}
