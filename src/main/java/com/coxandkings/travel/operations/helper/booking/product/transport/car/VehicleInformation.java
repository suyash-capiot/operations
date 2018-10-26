package com.coxandkings.travel.operations.helper.booking.product.transport.car;

public class VehicleInformation {
    private String vehicleName;
    private String travelClass;
    private String vehicleType;
    private Boolean airConditioned;
    private Boolean withChauffer;
    private String vehicleCategory;

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Boolean getAirConditioned() {
        return airConditioned;
    }

    public void setAirConditioned(Boolean airConditioned) {
        this.airConditioned = airConditioned;
    }

    public Boolean getWithChauffer() {
        return withChauffer;
    }

    public void setWithChauffer(Boolean withChauffer) {
        this.withChauffer = withChauffer;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public void setVehicleCategory(String vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    @Override
    public String toString() {
        return "VehicleInformation{" + "vehicleName='" + vehicleName + '\'' + ", travelClass='" + travelClass + '\'' + ", vehicleType='" + vehicleType + '\'' + ", airConditioned=" + airConditioned + ", withChauffer=" + withChauffer + '}';
    }
}
