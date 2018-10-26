package com.coxandkings.travel.operations.resource.managebookingstatus;

import com.coxandkings.travel.operations.model.core.OpsOrderStatus;

public class UpdateRoomStatusResource {

    private String roomID;
    private OpsOrderStatus status;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public OpsOrderStatus getStatus() {
        return status;
    }

    public void setStatus(OpsOrderStatus status) {
        this.status = status;
    }


}
