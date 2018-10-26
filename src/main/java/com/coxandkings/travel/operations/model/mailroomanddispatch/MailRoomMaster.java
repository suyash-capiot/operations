package com.coxandkings.travel.operations.model.mailroomanddispatch;


import com.coxandkings.travel.operations.enums.mailroomanddispatch.MailRoomStatus;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MasterEnumSerializer;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="manage_mail_room")
public class MailRoomMaster {

    @Id
    @GenericGenerator(name="id", strategy="com.coxandkings.travel.operations.generator.UniqueCodeGenerator")
    @GeneratedValue(generator="id")
    @Column(name = "master_room_id", nullable = false)
    private String id;
    private String mailRoomName;
    private String mailRoomShortName;
    private String buildingName;
    /*@OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "master_floor_id")
    @JsonManagedReference*/
    @ElementCollection
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
    @Transient
    private String _id;

    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    private ZonedDateTime effectiveFrom;

    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    private ZonedDateTime effectiveTo;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "master_room_id")
    @NotNull
    private List<MasterRoomContact> phoneList;
    private Long fax;
    /*@OneToOne
    @JoinColumn(name = "statusId")*/
    @JsonSerialize(using = MasterEnumSerializer.class)// Added this coz UI wants value as 'Active' and not ACTIVE
    private MailRoomStatus roomStatus;                  //Shashank asked for this change
    private String reason;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<EmployeeDetails> employeeDetails;

    //------------------WORKFLOW-----------------------
    @OneToOne(mappedBy = "mailRoomMaster", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private RequestLockObject lock;

    @Column
    private String lastModifiedByUserId;

    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @Column(name = "createdOn", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdOn;

    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @Column(name = "lastModifiedOn", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime lastModifiedOn;

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public ZonedDateTime getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(ZonedDateTime lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public RequestLockObject getLock() {
        return lock;
    }

    public void setLock(RequestLockObject lock) {
        this.lock = lock;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

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

    public Long getFax() {
        return fax;
    }

    public void setFax(Long fax) {
        this.fax = fax;
    }

    public MailRoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(MailRoomStatus roomStatus) {
        this.roomStatus = roomStatus;
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

    public ZonedDateTime getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(ZonedDateTime effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ZonedDateTime getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(ZonedDateTime effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public Set<EmployeeDetails> getEmployeeDetails() {
        return employeeDetails;
    }

    public void setEmployeeDetails(Set<EmployeeDetails> employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailRoomMaster that = (MailRoomMaster) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(mailRoomName, that.mailRoomName) &&
                Objects.equals(mailRoomShortName, that.mailRoomShortName) &&
                Objects.equals(buildingName, that.buildingName) &&
                Objects.equals(mailRoomOnFloor, that.mailRoomOnFloor) &&
                Objects.equals(addressLineOne, that.addressLineOne) &&
                Objects.equals(addressLineTwo, that.addressLineTwo) &&
                Objects.equals(addressLineThree, that.addressLineThree) &&
                Objects.equals(addressLineFour, that.addressLineFour) &&
                Objects.equals(addressLineFive, that.addressLineFive) &&
                Objects.equals(country, that.country) &&
                Objects.equals(state, that.state) &&
                Objects.equals(city, that.city) &&
                Objects.equals(area, that.area) &&
                Objects.equals(location, that.location) &&
                Objects.equals(postalCode, that.postalCode) &&
                Objects.equals(landMark, that.landMark) &&
                Objects.equals(effectiveFrom, that.effectiveFrom) &&
                Objects.equals(effectiveTo, that.effectiveTo) &&
                Objects.equals(phoneList, that.phoneList) &&
                Objects.equals(fax, that.fax) &&
                Objects.equals(roomStatus, that.roomStatus) &&
                Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, mailRoomName, mailRoomShortName, buildingName, mailRoomOnFloor, addressLineOne, addressLineTwo, addressLineThree, addressLineFour, addressLineFive, country, state, city, area, location, postalCode, landMark, effectiveFrom, effectiveTo, phoneList, fax, roomStatus, reason);
    }

    /*@Override
    public String toString() {
        return "MailRoomMaster{" +
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
                ", effectiveFrom=" + effectiveFrom +
                ", effectiveTo=" + effectiveTo +
                ", phoneList=" + phoneList +
                ", fax=" + fax +
                ", roomStatus=" + roomStatus +
                ", reason='" + reason + '\'' +
                '}';
    }*/
}
