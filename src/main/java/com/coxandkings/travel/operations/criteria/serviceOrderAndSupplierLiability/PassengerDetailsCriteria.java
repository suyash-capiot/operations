package com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.resource.BaseResource;

import java.math.BigDecimal;

public class PassengerDetailsCriteria extends BaseResource {
    private String id;
    private String passengerType;
    private BigDecimal ratePerPassenger;
    private Integer noOfPassengers;
    private BigDecimal supplierCostPrice;

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public BigDecimal getRatePerPassenger() {
        return ratePerPassenger;
    }

    public void setRatePerPassenger(BigDecimal ratePerPassenger) {
        this.ratePerPassenger = ratePerPassenger;
    }

    public Integer getNoOfPassenger() {
        return noOfPassengers;
    }

    public void setNoOfPassenger(Integer noOfPassenger) {
        this.noOfPassengers = noOfPassenger;
    }

    public Integer getNoOfPassengers() {
        return noOfPassengers;
    }

    public void setNoOfPassengers(Integer noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
    }

    public BigDecimal getSupplierCostPrice() {
        return supplierCostPrice;
    }

    public void setSupplierCostPrice(BigDecimal supplierCostPrice) {
        this.supplierCostPrice = supplierCostPrice;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
