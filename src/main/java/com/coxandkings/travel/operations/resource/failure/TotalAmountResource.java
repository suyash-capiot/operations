package com.coxandkings.travel.operations.resource.failure;

import java.util.Objects;

public class TotalAmountResource {

    private Double amount;
    private String roomTypeCode;
    private String roomCategoryCode;
    private String roomRef;
    private String roomTypeName;
    private String roomCategoryName;


    public TotalAmountResource(Double amount, String roomTypeCode, String roomCategoryCode, String roomRef, String roomTypeName, String roomCategoryName) {
        this.amount = amount;
        this.roomTypeCode = roomTypeCode;
        this.roomCategoryCode = roomCategoryCode;
        this.roomRef = roomRef;
        this.roomTypeName = roomTypeName;
        this.roomCategoryName = roomCategoryName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRoomTypeCode() {
        return roomTypeCode;
    }

    public void setRoomTypeCode(String roomTypeCode) {
        this.roomTypeCode = roomTypeCode;
    }

    public String getRoomCategoryCode() {
        return roomCategoryCode;
    }

    public void setRoomCategoryCode(String roomCategoryCode) {
        this.roomCategoryCode = roomCategoryCode;
    }

    public String getRoomRef() {
        return roomRef;
    }

    public void setRoomRef(String roomRef) {
        this.roomRef = roomRef;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomCategoryName() {
        return roomCategoryName;
    }

    public void setRoomCategoryName(String roomCategoryName) {
        this.roomCategoryName = roomCategoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TotalAmountResource that = (TotalAmountResource) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(roomTypeCode, that.roomTypeCode) &&
                Objects.equals(roomCategoryCode, that.roomCategoryCode) &&
                Objects.equals(roomRef, that.roomRef) &&
                Objects.equals(roomTypeName, that.roomTypeName) &&
                Objects.equals(roomCategoryName, that.roomCategoryName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(amount, roomTypeCode, roomCategoryCode, roomRef, roomTypeName, roomCategoryName);
    }
}
