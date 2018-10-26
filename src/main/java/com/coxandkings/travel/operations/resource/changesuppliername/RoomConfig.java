
package com.coxandkings.travel.operations.resource.changesuppliername;

import java.util.List;

public class RoomConfig {

    private Integer adultCount;
    private List<Object> childAges = null;
    private RoomInfo roomInfo;

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public List<Object> getChildAges() {
        return childAges;
    }

    public void setChildAges(List<Object> childAges) {
        this.childAges = childAges;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

}
