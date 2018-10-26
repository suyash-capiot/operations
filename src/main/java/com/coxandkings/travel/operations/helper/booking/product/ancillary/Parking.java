package com.coxandkings.travel.operations.helper.booking.product.ancillary;

import com.coxandkings.travel.operations.helper.booking.product.transport.car.VehicleInformation;

public class Parking extends Ancillary {
    private VehicleInformation vehicleInformation;

    public VehicleInformation getVehicleInformation() {
        return vehicleInformation;
    }

    public void setVehicleInformation(VehicleInformation vehicleInformation) {
        this.vehicleInformation = vehicleInformation;
    }
}
