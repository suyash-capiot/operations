package com.coxandkings.travel.operations.model.commercialstatements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "commercial_statement_passenger_details")
public class PassengerDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "passenger_name")
    private String passengerName;

    @Column(name = "passenget_type")
    private String passengerType;

    @ManyToOne
    @JsonIgnore
    private OrderCommercialDetails orderCommercialDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public OrderCommercialDetails getOrderCommercialDetails() {
        return orderCommercialDetails;
    }

    public void setOrderCommercialDetails(OrderCommercialDetails orderCommercialDetails) {
        this.orderCommercialDetails = orderCommercialDetails;
    }
}
