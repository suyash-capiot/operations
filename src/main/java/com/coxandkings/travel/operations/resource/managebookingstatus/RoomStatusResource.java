package com.coxandkings.travel.operations.resource.managebookingstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userID",
        "roomID",
        "status"
})
public class RoomStatusResource {

    @JsonProperty("userID")
    private String userID;
    @JsonProperty("roomID")
    private String roomID;
    @JsonProperty("status")
    private String status;

    @JsonProperty("userID")
    public String getUserID() {
        return userID;
    }

    @JsonProperty("userID")
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @JsonProperty("roomID")
    public String getRoomID() {
        return roomID;
    }

    @JsonProperty("roomID")
    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }


}

