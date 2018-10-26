package com.coxandkings.travel.operations.resource.failure;

import com.coxandkings.travel.operations.enums.failure.FailureActions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetailsResource {

    private String orderId;

    private List<OrderSummary> orderSummary;

    private String orderStatus;

    private String supplierName;

    private FailureActions updateFlag;

    private int paxCount;

    private String leadPaxName;

    private String paymentStatus;



    public int getPaxCount() {
        return paxCount;
    }

    public void setPaxCount(int paxCount) {
        this.paxCount = paxCount;
    }

    public String getLeadPaxName() {
        return leadPaxName;
    }

    public void setLeadPaxName(String leadPaxName) {
        this.leadPaxName = leadPaxName;
    }
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderSummary> getOrderSummary() {
        return orderSummary;
    }

    public void setOrderSummary(List<OrderSummary> orderSummary) {
        this.orderSummary = orderSummary;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public FailureActions getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(FailureActions updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
