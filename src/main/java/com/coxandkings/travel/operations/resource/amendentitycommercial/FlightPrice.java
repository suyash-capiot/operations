package com.coxandkings.travel.operations.resource.amendentitycommercial;

import com.coxandkings.travel.operations.model.core.OpsFlightSupplierPriceInfo;
import com.coxandkings.travel.operations.model.core.OpsFlightTotalPriceInfo;

public class FlightPrice {

    private OpsFlightTotalPriceInfo orderTotalPriceInfo;
    private OpsFlightSupplierPriceInfo orderSupplierPriceInfo;

    public OpsFlightTotalPriceInfo getOrderTotalPriceInfo() {
        return orderTotalPriceInfo;
    }

    public void setOrderTotalPriceInfo(OpsFlightTotalPriceInfo orderTotalPriceInfo) {
        this.orderTotalPriceInfo = orderTotalPriceInfo;
    }

    public OpsFlightSupplierPriceInfo getOrderSupplierPriceInfo() {
        return orderSupplierPriceInfo;
    }

    public void setOrderSupplierPriceInfo(OpsFlightSupplierPriceInfo orderSupplierPriceInfo) {
        this.orderSupplierPriceInfo = orderSupplierPriceInfo;
    }


}
