package com.coxandkings.travel.operations.model.commercialstatements;

import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "commercial_statement_details")
public class CommercialStatementDetails {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime bookingDate;

    @Column(name = "booking_id")
    private String bookingRefNum;

    @OneToMany(mappedBy = "commercialStatementDetails", cascade = CascadeType.ALL)
    private Set<OrderCommercialDetails> orderCommercialDetails;

    @ManyToOne
    @JsonIgnore
    private BaseCommercialDetails commercialStatement;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(ZonedDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingRefNum() {
        return bookingRefNum;
    }

    public void setBookingRefNum(String bookingRefNum) {
        this.bookingRefNum = bookingRefNum;
    }

    public BaseCommercialDetails getCommercialStatement() {
        return commercialStatement;
    }

    public void setCommercialStatement(BaseCommercialDetails commercialStatement) {
        this.commercialStatement = commercialStatement;
    }

    public Set<OrderCommercialDetails> getOrderCommercialDetails() {
        return orderCommercialDetails;
    }

    public void setOrderCommercialDetails(Set<OrderCommercialDetails> orderCommercialDetails) {
        this.orderCommercialDetails = orderCommercialDetails;
    }
}
