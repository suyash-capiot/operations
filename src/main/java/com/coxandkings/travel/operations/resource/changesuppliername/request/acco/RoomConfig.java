
package com.coxandkings.travel.operations.resource.changesuppliername.request.acco;

import java.util.List;

public class RoomConfig {

    private Integer adultCount;
    private List<Integer> childAges = null;
    private RoomInfo roomInfo;

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public List<Integer> getChildAges() {
        return childAges;
    }

    public void setChildAges(List<Integer> childAges) {
        this.childAges = childAges;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

}
