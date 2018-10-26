package com.coxandkings.travel.operations.model.commercialstatements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "order_commercial_statement_details")
public class OrderCommercialDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;
    private String orderId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderCommercialDetails")
    private Set<PassengerDetails> passengerDetailsSet;

    @Column(name = "commercial_value")
    private BigDecimal commercialValue;

    @Column(name = "service_tax")
    private BigDecimal serviceTax;

    @Column(name = "number_of_passengers")
    private Integer numberOfPassengers;

    @Column(name = "supplierOrClientCostPrice")
    private BigDecimal supplierOrClientCostPrice;

    @ManyToOne
    @JsonIgnore
    private CommercialStatementDetails commercialStatementDetails;

    public BigDecimal getSupplierOrClientCostPrice() {
        return supplierOrClientCostPrice;
    }

    public void setSupplierOrClientCostPrice(BigDecimal supplierOrClientCostPrice) {
        this.supplierOrClientCostPrice = supplierOrClientCostPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Set<PassengerDetails> getPassengerDetailsSet() {
        return passengerDetailsSet;
    }

    public void setPassengerDetailsSet(Set<PassengerDetails> passengerDetailsSet) {
        this.passengerDetailsSet = passengerDetailsSet;
    }

    public BigDecimal getCommercialValue() {
        return commercialValue;
    }

    public void setCommercialValue(BigDecimal commercialValue) {
        this.commercialValue = commercialValue;
    }

    public Integer getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(Integer numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public CommercialStatementDetails getCommercialStatementDetails() {
        return commercialStatementDetails;
    }

    public void setCommercialStatementDetails(CommercialStatementDetails commercialStatementDetails) {
        this.commercialStatementDetails = commercialStatementDetails;
    }

    public BigDecimal getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(BigDecimal serviceTax) {
        this.serviceTax = serviceTax;
    }
}
