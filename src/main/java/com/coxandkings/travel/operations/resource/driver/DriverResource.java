package com.coxandkings.travel.operations.resource.driver;

import com.coxandkings.travel.operations.resource.BaseResource;
import org.jgroups.annotations.Property;

import java.util.List;

public class DriverResource extends BaseResource {

    private String productId;
    private String vehicleCategoryId;
    private String vehicleName;
    private String vehicleNumber;
    private String driverName;
    private String mobileNumber;
//    private String countryCode;
    private String driverStatus;
//    private Boolean fromWorkflow;
    private List<String> status;
    private boolean linkSent;
    private String extranetTodoId;
    private String userTodoId;

    @Property(name = "linkSent")
    public boolean getLinkSent() {
        return linkSent;
    }

    @Property(name = "linkSent")
    public void setLinkSent(boolean linkSent) {
        this.linkSent = linkSent;
    }

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVehicleCategoryId() {
        return vehicleCategoryId;
    }

    public void setVehicleCategoryId(String vehicleCategoryId) {
        this.vehicleCategoryId = vehicleCategoryId;
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

   /* public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }*/

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }
}
