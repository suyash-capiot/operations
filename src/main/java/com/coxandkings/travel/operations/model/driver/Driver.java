package com.coxandkings.travel.operations.model.driver;

import com.coxandkings.travel.operations.enums.driver.DriverStatus;
import com.coxandkings.travel.operations.model.BaseModel;
import org.jgroups.annotations.Property;

import javax.persistence.*;

@Entity
@Table(name = "driver")
public class Driver  extends BaseModel {

    @Column(name="bookingRefId")
    private String bookingRefId;

    @Column(name = "productId")
    private String productId;

    @Column(name = "vehicleCategoryId")
    private String vehicleCategoryId;

    @Column(name = "vehicleName")
    private String vehicleName;

    @Column(name = "vehicleNumber")
    private String vehicleNumber;

    @Column(name = "driverName")
    private String driverName;

    @Column(name = "mobileNumber")
//    @Pattern(regexp = "[0-9]{10}")
    private String mobileNumber;

  /*  @Column(name = "countryCode")
    @Pattern(regexp = "[+^0-9]{3}")
    private String countryCode;*/

    @Enumerated(value = EnumType.STRING)
    @Column(name = "driverStatus")
    private DriverStatus driverStatusType;

    @Column(name = "linkSent")
    private boolean linkSent;

    @Column(name = "extranetTodoId")
    private String extranetTodoId;

    @Column(name = "userTodoId")
    private String userTodoId;

    public String getExtranetTodoId() {
        return extranetTodoId;
    }

    public void setExtranetTodoId(String extranetTodoId) {
        this.extranetTodoId = extranetTodoId;
    }

    public String getUserTodoId() {
        return userTodoId;
    }

    public void setUserTodoId(String userTodoId) {
        this.userTodoId = userTodoId;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

  /*  public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }*/

    public String getVehicleCategoryId() {
        return vehicleCategoryId;
    }

    public void setVehicleCategoryId(String vehicleCategoryId) {
        this.vehicleCategoryId = vehicleCategoryId;
    }

    public DriverStatus getDriverStatusType() {
        return driverStatusType;
    }

    public void setDriverStatusType(DriverStatus driverStatusType) {
        this.driverStatusType = driverStatusType;
    }

    @Property(name = "linkSent")
    public boolean getLinkSent() {
        return linkSent;
    }

    @Property(name = "linkSent")
    public void setLinkSent(boolean linkSent) {
        this.linkSent = linkSent;
    }
}
