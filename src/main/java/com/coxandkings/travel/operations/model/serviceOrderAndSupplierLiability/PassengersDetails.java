package com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "passengers")
@Audited
public class PassengersDetails implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column
    private String id;

    @Column
    @NotNull(message = "passenger type should not be null")
    private String passengerType;

    @Column
    @NotNull(message = "rate per passenger should not be null")
    private BigDecimal ratePerPassenger;

    @Column
    @NotNull(message = "no of passengers should not be null")
    private Integer noOfPassengers;

    @Column
    @NotNull(message = "supplier cost price per passenger should not be null")
    private BigDecimal supplierCostPrice;

    @Column
    private String roomCategoryOrCabinCategory;

    @Column
    private String roomTypeOrCabinType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplierPricingId", nullable = false, insertable = true, updatable = true)
    @JsonIgnore
    private SupplierPricing supplierPricing;


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

    public BigDecimal getSupplierCostPrice() {
        return supplierCostPrice;
    }

    public void setSupplierCostPrice(BigDecimal supplierCostPrice) {
        this.supplierCostPrice = supplierCostPrice;
    }

    public Integer getNoOfPassengers() {
        return noOfPassengers;
    }

    public void setNoOfPassengers(Integer noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
    }

    public SupplierPricing getSupplierPricing() {
        return supplierPricing;
    }

    public void setSupplierPricing(SupplierPricing supplierPricing) {
        this.supplierPricing = supplierPricing;
    }

    public String getRoomCategoryOrCabinCategory() {
        return roomCategoryOrCabinCategory;
    }

    public void setRoomCategoryOrCabinCategory(String roomCategoryOrCabinCategory) {
        this.roomCategoryOrCabinCategory = roomCategoryOrCabinCategory;
    }

    public String getRoomTypeOrCabinType() {
        return roomTypeOrCabinType;
    }

    public void setRoomTypeOrCabinType(String roomTypeOrCabinType) {
        this.roomTypeOrCabinType = roomTypeOrCabinType;
    }

    @Override
    public String toString() {
        return "PassengersDetails{" +
                "id='" + id + '\'' +
                ", passengerType='" + passengerType + '\'' +
                ", ratePerPassenger=" + ratePerPassenger +
                ", noOfPassengers=" + noOfPassengers +
                ", supplierCostPrice=" + supplierCostPrice +
                ", supplierPricingList=" + supplierPricing +
                '}';
    }

}
