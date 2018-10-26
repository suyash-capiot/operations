package com.coxandkings.travel.operations.criteria.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.BaseCriteria;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.MailRoomStatus;
/*import com.coxandkings.travel.operations.model.mailroomanddispatch.RoomStatus;*/

public class MailRoomSearchCriteria extends BaseCriteria {
    private String Id;
    private String mailRoomName;
    private String country;
    private String state;
    private String city;
    private MailRoomStatus roomStatus;
    private Integer pageSize;
    private Integer pageNumber;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMailRoomName() {
        return mailRoomName;
    }

    public void setMailRoomName(String mailRoomName) {
        this.mailRoomName = mailRoomName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public MailRoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(MailRoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Integer getPageNumber() {
        return pageNumber;
    }

    @Override
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
