
package com.coxandkings.travel.operations.resource.bookingenginedb.air;

import com.coxandkings.travel.ext.model.be.OrderClientCommercial;
import com.coxandkings.travel.ext.model.be.OrderSupplierCommercial;
import com.coxandkings.travel.ext.model.be.OrderSupplierPriceInfo;
import com.coxandkings.travel.ext.model.be.OrderTotalPriceInfo;

import java.util.List;

public class AirOrderPriceDetail {

    private String orderID;
    private List<OrderClientCommercial> orderClientCommercials = null;
    private OrderSupplierPriceInfo orderSupplierPriceInfo;
    private OrderTotalPriceInfo orderTotalPriceInfo;
    private List<OrderSupplierCommercial> orderSupplierCommercials = null;
    private String userID;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public List<OrderClientCommercial> getOrderClientCommercials() {
        return orderClientCommercials;
    }

    public void setOrderClientCommercials(List<OrderClientCommercial> orderClientCommercials) {
        this.orderClientCommercials = orderClientCommercials;
    }

    public OrderSupplierPriceInfo getOrderSupplierPriceInfo() {
        return orderSupplierPriceInfo;
    }

    public void setOrderSupplierPriceInfo(OrderSupplierPriceInfo orderSupplierPriceInfo) {
        this.orderSupplierPriceInfo = orderSupplierPriceInfo;
    }

    public OrderTotalPriceInfo getOrderTotalPriceInfo() {
        return orderTotalPriceInfo;
    }

    public void setOrderTotalPriceInfo(OrderTotalPriceInfo orderTotalPriceInfo) {
        this.orderTotalPriceInfo = orderTotalPriceInfo;
    }

    public List<OrderSupplierCommercial> getOrderSupplierCommercials() {
        return orderSupplierCommercials;
    }

    public void setOrderSupplierCommercials(List<OrderSupplierCommercial> orderSupplierCommercials) {
        this.orderSupplierCommercials = orderSupplierCommercials;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


}
