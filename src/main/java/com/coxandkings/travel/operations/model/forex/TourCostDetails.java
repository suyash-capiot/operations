package com.coxandkings.travel.operations.model.forex;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class TourCostDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    private BigDecimal buyingAmount;
    private Float rateOfExchange;
    private BigDecimal inrEquivalent;// currency INR
    private BigDecimal commission;
    private String currency;
    private String billTo;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "forex_passenger_id")
    private ForexPassenger forexPassenger;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getBuyingAmount() {
        return buyingAmount;
    }

    public void setBuyingAmount(BigDecimal buyingAmount) {
        this.buyingAmount = buyingAmount;
    }

    public Float getRateOfExchange() {
        return rateOfExchange;
    }

    public void setRateOfExchange(Float rateOfExchange) {
        this.rateOfExchange = rateOfExchange;
    }

    public BigDecimal getInrEquivalent() {
        return inrEquivalent;
    }

    public void setInrEquivalent(BigDecimal inrEquivalent) {
        this.inrEquivalent = inrEquivalent;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ForexPassenger getForexPassenger() {
        return forexPassenger;
    }

    public void setForexPassenger(ForexPassenger forexPassenger) {
        this.forexPassenger = forexPassenger;
    }
}
