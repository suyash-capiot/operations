package com.coxandkings.travel.operations.resource.productsharing;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userID",
        "roomID",
        "isSharable"
})
public class SharableRequest {

    @JsonProperty("userID")
    private String userID;
    @JsonProperty("roomID")
    private String roomID;
    @JsonProperty("isSharable")
    private Boolean isSharable;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("isSharable")
    public Boolean getIsSharable() {
        return isSharable;
    }

    @JsonProperty("isSharable")
    public void setIsSharable(Boolean isSharable) {
        this.isSharable = isSharable;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "SharableRequest{" +
                "userID='" + userID + '\'' +
                ", roomID='" + roomID + '\'' +
                ", isSharable=" + isSharable +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}