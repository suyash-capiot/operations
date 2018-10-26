
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "roomTypeCode",
    "roomCategoryID",
    "roomRef",
    "roomTypeName",
    "roomCategoryName"
})
public class RoomTypeInfo implements Serializable
{

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
    @JsonProperty("InvBlockCode")
    private String InvBlockCode;
    @JsonProperty("cabinnumber")
    private String cabinnumber;
    private final static long serialVersionUID = -1045130843003506268L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RoomTypeInfo() {
    }

    /**
     * 
     * @param roomTypeCode
     * @param roomCategoryID
     * @param roomRef
     * @param roomTypeName
     * @param roomCategoryName
     */
    public RoomTypeInfo(String roomTypeCode, String roomCategoryID, String roomRef, String roomTypeName, String roomCategoryName) {
        super();
        this.roomTypeCode = roomTypeCode;
        this.roomCategoryID = roomCategoryID;
        this.roomRef = roomRef;
        this.roomTypeName = roomTypeName;
        this.roomCategoryName = roomCategoryName;
    }

    @JsonProperty("roomTypeCode")
    public String getRoomTypeCode() {
        return roomTypeCode;
    }

    @JsonProperty("roomTypeCode")
    public void setRoomTypeCode(String roomTypeCode) {
        this.roomTypeCode = roomTypeCode;
    }

    @JsonProperty("roomCategoryID")
    public String getRoomCategoryID() {
        return roomCategoryID;
    }

    @JsonProperty("roomCategoryID")
    public void setRoomCategoryID(String roomCategoryID) {
        this.roomCategoryID = roomCategoryID;
    }

    @JsonProperty("roomRef")
    public String getRoomRef() {
        return roomRef;
    }

    @JsonProperty("roomRef")
    public void setRoomRef(String roomRef) {
        this.roomRef = roomRef;
    }

    @JsonProperty("roomTypeName")
    public String getRoomTypeName() {
        return roomTypeName;
    }

    @JsonProperty("roomTypeName")
    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    @JsonProperty("roomCategoryName")
    public String getRoomCategoryName() {
        return roomCategoryName;
    }

    @JsonProperty("roomCategoryName")
    public void setRoomCategoryName(String roomCategoryName) {
        this.roomCategoryName = roomCategoryName;
    }

    public String getInvBlockCode() {
        return InvBlockCode;
    }

    public void setInvBlockCode(String invBlockCode) {
        InvBlockCode = invBlockCode;
    }

    public String getCabinnumber() {
        return cabinnumber;
    }

    public void setCabinnumber(String cabinnumber) {
        this.cabinnumber = cabinnumber;
    }
	
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RoomTypeInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("roomTypeCode");
        sb.append('=');
        sb.append(((this.roomTypeCode == null)?"<null>":this.roomTypeCode));
        sb.append(',');
        sb.append("roomCategoryID");
        sb.append('=');
        sb.append(((this.roomCategoryID == null)?"<null>":this.roomCategoryID));
        sb.append(',');
        sb.append("roomRef");
        sb.append('=');
        sb.append(((this.roomRef == null)?"<null>":this.roomRef));
        sb.append(',');
        sb.append("roomTypeName");
        sb.append('=');
        sb.append(((this.roomTypeName == null)?"<null>":this.roomTypeName));
        sb.append(',');
        sb.append("roomCategoryName");
        sb.append('=');
        sb.append(((this.roomCategoryName == null)?"<null>":this.roomCategoryName));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.roomTypeName == null)? 0 :this.roomTypeName.hashCode()));
        result = ((result* 31)+((this.roomCategoryName == null)? 0 :this.roomCategoryName.hashCode()));
        result = ((result* 31)+((this.roomTypeCode == null)? 0 :this.roomTypeCode.hashCode()));
        result = ((result* 31)+((this.roomCategoryID == null)? 0 :this.roomCategoryID.hashCode()));
        result = ((result* 31)+((this.roomRef == null)? 0 :this.roomRef.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RoomTypeInfo) == false) {
            return false;
        }
        RoomTypeInfo rhs = ((RoomTypeInfo) other);
        return ((((((this.roomTypeName == rhs.roomTypeName)||((this.roomTypeName!= null)&&this.roomTypeName.equals(rhs.roomTypeName)))&&((this.roomCategoryName == rhs.roomCategoryName)||((this.roomCategoryName!= null)&&this.roomCategoryName.equals(rhs.roomCategoryName))))&&((this.roomTypeCode == rhs.roomTypeCode)||((this.roomTypeCode!= null)&&this.roomTypeCode.equals(rhs.roomTypeCode))))&&((this.roomCategoryID == rhs.roomCategoryID)||((this.roomCategoryID!= null)&&this.roomCategoryID.equals(rhs.roomCategoryID))))&&((this.roomRef == rhs.roomRef)||((this.roomRef!= null)&&this.roomRef.equals(rhs.roomRef))));
    }

}
