package com.coxandkings.travel.operations.model.sellingPrice;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class AccommodationDiscount extends Discount {
    private String roomId;

    public AccommodationDiscount() {
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
