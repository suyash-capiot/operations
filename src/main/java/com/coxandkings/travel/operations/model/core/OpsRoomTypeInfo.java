package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsRoomTypeInfo implements Serializable {

    @JsonProperty("roomTypeCode")
    private String roomTypeCode;

    @JsonProperty("roomCategoryID")
    private String roomCategoryID;

    @JsonProperty("roomRef")
    private String roomRef;

    @JsonProperty("roomTypeName")
    private String roomTypeName;

    @JsonProperty("roomCategoryName")
    private String roomCategoryName;

    private final static long serialVersionUID = 663257694310194942L;

    public OpsRoomTypeInfo() {
    }

    public OpsRoomTypeInfo(String roomTypeCode, String roomCategoryID, String roomRef, String roomTypeName, String roomCategoryName) {
        this.roomTypeCode = roomTypeCode;
        this.roomCategoryID = roomCategoryID;
        this.roomRef = roomRef;
        this.roomTypeName = roomTypeName;
        this.roomCategoryName = roomCategoryName;
    }

//    public OpsRoomTypeInfo(RoomTypeInfo roomTypeInfo) {
//        this.roomTypeCode = roomTypeInfo.getRoomTypeCode();
//        this.roomCategoryID = roomTypeInfo.getRoomCategoryID();
//        this.roomRef = roomTypeInfo.getRoomRef();
//        this.roomTypeName = roomTypeInfo.getRoomTypeName();
//        this.roomCategoryName = roomTypeInfo.getRoomCategoryName();
//    }

    public String getRoomTypeCode() {
        return roomTypeCode;
    }

    public void setRoomTypeCode(String roomTypeCode) {
        this.roomTypeCode = roomTypeCode;
    }

    public String getRoomCategoryID() {
        return roomCategoryID;
    }

    public void setRoomCategoryID(String roomCategoryID) {
        this.roomCategoryID = roomCategoryID;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsRoomTypeInfo that = (OpsRoomTypeInfo) o;
        return Objects.equals(roomTypeCode, that.roomTypeCode) &&
                Objects.equals(roomCategoryID, that.roomCategoryID) &&
                Objects.equals(roomRef, that.roomRef) &&
                Objects.equals(roomTypeName, that.roomTypeName) &&
                Objects.equals(roomCategoryName, that.roomCategoryName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(roomTypeCode, roomCategoryID, roomRef, roomTypeName, roomCategoryName);
    }
}
