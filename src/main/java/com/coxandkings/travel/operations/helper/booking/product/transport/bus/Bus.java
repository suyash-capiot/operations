package com.coxandkings.travel.operations.helper.booking.product.transport.bus;

import com.coxandkings.travel.operations.helper.booking.product.transport.Transfer;

public class Bus extends Transfer {
    private String serviceProviderId;
    private String vehicleName;
    private String seatingType;
    private Integer numberOfSeats;
    private Boolean withAC;

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getSeatingType() {
        return seatingType;
    }

    public void setSeatingType(String seatingType) {
        this.seatingType = seatingType;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Boolean getWithAC() {
        return withAC;
    }

    public void setWithAC(Boolean withAC) {
        this.withAC = withAC;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }
}
