package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.MailRoomStatus;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.WorkflowEnums;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.mailroomanddispatch.MasterRoomContact;
import com.coxandkings.travel.operations.resource.BaseResource;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/*import com.coxandkings.travel.operations.model.mailroomanddispatch.RoomStatus;*/

public class MailRoomMasterResource extends BaseResource {
    private String id;
    private String mailRoomName;
    private String mailRoomShortName;
    private String buildingName;
    private List<String> mailRoomOnFloor;
    private String addressLineOne;
    private String addressLineTwo;
    private String addressLineThree;
    private String addressLineFour;
    private String addressLineFive;
    private String country;
    private String state;
    private String city;
    private String area;
    private String location;
    private String postalCode;
    private String landMark;
    private List<MasterRoomContact> phoneList;
    private Long fax;
    private MailRoomStatus roomStatus;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime effectiveFrom;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime effectiveTo;
    private String reason;

    //WorkFlow Implementations
    private String action;
    private WorkflowEnums status;
    private String lastModifiedByUserId;

    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime lastModifiedOn;

    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime createdOn;
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public WorkflowEnums getStatus() {
        return status;
    }

    public void setStatus(WorkflowEnums status) {
        this.status = status;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public ZonedDateTime getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(ZonedDateTime lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getMailRoomName() {
        return mailRoomName;
    }

    public void setMailRoomName(String mailRoomName) {
        this.mailRoomName = mailRoomName;
    }

    public String getMailRoomShortName() {
        return mailRoomShortName;
    }

    public void setMailRoomShortName(String mailRoomShortName) {
        this.mailRoomShortName = mailRoomShortName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public List<String> getMailRoomOnFloor() {
        return mailRoomOnFloor;
    }

    public void setMailRoomOnFloor(List<String> mailRoomOnFloor) {
        this.mailRoomOnFloor = mailRoomOnFloor;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getFax() {
        return fax;
    }

    public void setFax(Long fax) {
        this.fax = fax;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public List<MasterRoomContact> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<MasterRoomContact> phoneList) {
        this.phoneList = phoneList;
    }

    public MailRoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = MailRoomStatus.getMailRoomStatus(roomStatus);
    }

    public String getAddressLineThree() {
        return addressLineThree;
    }

    public void setAddressLineThree(String addressLineThree) {
        this.addressLineThree = addressLineThree;
    }

    public String getAddressLineFour() {
        return addressLineFour;
    }

    public void setAddressLineFour(String addressLineFour) {
        this.addressLineFour = addressLineFour;
    }

    public String getAddressLineFive() {
        return addressLineFive;
    }

    public void setAddressLineFive(String addressLineFive) {
        this.addressLineFive = addressLineFive;
    }

    public ZonedDateTime getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(ZonedDateTime effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public ZonedDateTime getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(ZonedDateTime effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailRoomMasterResource resource = (MailRoomMasterResource) o;
        return Objects.equals(id, resource.id) &&
                Objects.equals(mailRoomName, resource.mailRoomName) &&
                Objects.equals(mailRoomShortName, resource.mailRoomShortName) &&
                Objects.equals(buildingName, resource.buildingName) &&
                Objects.equals(mailRoomOnFloor, resource.mailRoomOnFloor) &&
                Objects.equals(addressLineOne, resource.addressLineOne) &&
                Objects.equals(addressLineTwo, resource.addressLineTwo) &&
                Objects.equals(addressLineThree, resource.addressLineThree) &&
                Objects.equals(addressLineFour, resource.addressLineFour) &&
                Objects.equals(addressLineFive, resource.addressLineFive) &&
                Objects.equals(country, resource.country) &&
                Objects.equals(state, resource.state) &&
                Objects.equals(city, resource.city) &&
                Objects.equals(area, resource.area) &&
                Objects.equals(location, resource.location) &&
                Objects.equals(postalCode, resource.postalCode) &&
                Objects.equals(landMark, resource.landMark) &&
                Objects.equals(phoneList, resource.phoneList) &&
                Objects.equals(fax, resource.fax) &&
                Objects.equals(roomStatus, resource.roomStatus) &&
                Objects.equals(effectiveFrom, resource.effectiveFrom) &&
                Objects.equals(reason, resource.reason);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, mailRoomName, mailRoomShortName, buildingName, mailRoomOnFloor, addressLineOne, addressLineTwo, addressLineThree, addressLineFour, addressLineFive, country, state, city, area, location, postalCode, landMark, phoneList, fax, roomStatus, effectiveFrom, reason);
    }

    @Override
    public String toString() {
        return "MailRoomMasterResource{" +
                "id='" + id + '\'' +
                ", mailRoomName='" + mailRoomName + '\'' +
                ", mailRoomShortName='" + mailRoomShortName + '\'' +
                ", buildingName='" + buildingName + '\'' +
                ", mailRoomOnFloor=" + mailRoomOnFloor +
                ", addressLineOne='" + addressLineOne + '\'' +
                ", addressLineTwo='" + addressLineTwo + '\'' +
                ", addressLineThree='" + addressLineThree + '\'' +
                ", addressLineFour='" + addressLineFour + '\'' +
                ", addressLineFive='" + addressLineFive + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", location='" + location + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", landMark='" + landMark + '\'' +
                ", phoneList=" + phoneList +
                ", fax=" + fax +
                ", roomStatus='" + roomStatus + '\'' +
                ", effectiveFrom=" + effectiveFrom +
                ", reason='" + reason + '\'' +
                '}';
    }
}
