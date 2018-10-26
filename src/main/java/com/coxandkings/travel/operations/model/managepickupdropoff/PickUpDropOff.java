package com.coxandkings.travel.operations.model.managepickupdropoff;


import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "Manage_PickUp_DropOff")
public class PickUpDropOff
{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    private String bookingRefId;

    private String orderId;

    private String pickUpType;

    private String dropOffType;

    private String pickUpPointName;

    @Column(name = "pickUpTime")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime pickUpTime;

    private String dropOffPointName;

    private String viaPoint;

    private String vehicleCategory;

    private String vehicleName;

    private Boolean airCondition;

    private Boolean withChauffer;

    public String getPickUpType() {
        return pickUpType;
    }

    public void setPickUpType(String pickUpType) {
        this.pickUpType = pickUpType;
    }

    public String getDropOffType() {
        return dropOffType;
    }

    public void setDropOffType(String dropOffType) {
        this.dropOffType = dropOffType;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPickUpPointName() {
        return pickUpPointName;
    }

    public void setPickUpPointName(String pickUpPointName) {
        this.pickUpPointName = pickUpPointName;
    }

    public ZonedDateTime getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(ZonedDateTime pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getDropOffPointName() {
        return dropOffPointName;
    }

    public void setDropOffPointName(String dropOffPointName) {
        this.dropOffPointName = dropOffPointName;
    }

    public String getViaPoint() {
        return viaPoint;
    }

    public void setViaPoint(String viaPoint) {
        this.viaPoint = viaPoint;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public void setVehicleCategory(String vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Boolean getAirCondition() {
        return airCondition;
    }

    public void setAirCondition(Boolean airCondition) {
        this.airCondition = airCondition;
    }

    public Boolean getWithChauffer() {
        return withChauffer;
    }

    public void setWithChauffer(Boolean withChauffer) {
        this.withChauffer = withChauffer;
    }

}
