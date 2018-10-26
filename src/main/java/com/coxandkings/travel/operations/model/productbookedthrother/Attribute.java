package com.coxandkings.travel.operations.model.productbookedthrother;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Attribute {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Enumerated(value = EnumType.STRING)
    private OpsProductSubCategory productCategorySubType;

    @Column(name = "bookingRefId")
    private String bookingRefId;

    @Column(name = "orderId")
    private String orderId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime departureDateTime;

    @Column(name = "linkSent")
    private boolean linkSent;


    public boolean getLinkSent() {
        return linkSent;
    }

    public void setLinkSent(boolean linkSent) {
        this.linkSent = linkSent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OpsProductSubCategory getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(OpsProductSubCategory productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public ZonedDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(ZonedDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
