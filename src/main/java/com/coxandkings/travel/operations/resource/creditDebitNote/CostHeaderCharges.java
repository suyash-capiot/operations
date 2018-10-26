package com.coxandkings.travel.operations.resource.creditDebitNote;

public class CostHeaderCharges {

    private String id;
    private String costHeader;
    private String passengerType;
    private Integer passengerQuantity;
    private String currency;
    private Double amount;
    private Double totalAmount;

    public String getCostHeader() {
        return costHeader;
    }

    public void setCostHeader(String costHeader) {
        this.costHeader = costHeader;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public Integer getPassengerQuantity() {
        return passengerQuantity;
    }

    public void setPassengerQuantity(Integer passengerQuantity) {
        this.passengerQuantity = passengerQuantity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
