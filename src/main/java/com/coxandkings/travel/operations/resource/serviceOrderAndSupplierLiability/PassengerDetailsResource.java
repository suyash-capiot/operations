package com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability;

import java.math.BigDecimal;
import java.util.Set;

public class PassengerDetailsResource {

    private String id;
    private String passengerType;
    private BigDecimal ratePerPassenger;
    private Integer noOfPassengers;
    private BigDecimal supplierCostPrice;
    private Set<SupplierPricingResource> supplierPricingResourcesSet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Set<SupplierPricingResource> getSupplierPricingResourcesSet() {
        return supplierPricingResourcesSet;
    }

    public void setSupplierPricingResourcesSet(Set<SupplierPricingResource> supplierPricingResourcesSet) {
        this.supplierPricingResourcesSet = supplierPricingResourcesSet;
    }
}
