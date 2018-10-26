package com.coxandkings.travel.operations.helper.booking.product.transport.car;

import com.coxandkings.travel.operations.helper.booking.Location;
import com.coxandkings.travel.operations.helper.booking.product.transport.Transfer;

import java.util.Set;

public class Car extends Transfer {

    private String transferTypeId;//private or shared
    private String journeyTypeId;//one-way or return
    private String rateDefinedFor;//Inter-city, Car package, Car transfer
    private String typeCode;
    private String transferTypeCode;

    private VehicleInformation vehicleInformation;
    private Set<Location> viaLocations;

    public String getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(String transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getJourneyTypeId() {
        return journeyTypeId;
    }

    public void setJourneyTypeId(String journeyTypeId) {
        this.journeyTypeId = journeyTypeId;
    }

    public String getRateDefinedFor() {
        return rateDefinedFor;
    }

    public void setRateDefinedFor(String rateDefinedFor) {
        this.rateDefinedFor = rateDefinedFor;
    }

    public VehicleInformation getVehicleInformation() {
        return vehicleInformation;
    }

    public void setVehicleInformation(VehicleInformation vehicleInformation) {
        this.vehicleInformation = vehicleInformation;
    }

    public Set<Location> getViaLocations() {
        return viaLocations;
    }

    public void setViaLocations(Set<Location> viaLocations) {
        this.viaLocations = viaLocations;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTransferTypeCode() {
        return transferTypeCode;
    }

    public void setTransferTypeCode(String transferTypeCode) {
        this.transferTypeCode = transferTypeCode;
    }

    @Override
    public String toString() {
        return "Car{" +
                "transferTypeId='" + transferTypeId + '\'' +
                ", journeyTypeId='" + journeyTypeId + '\'' +
                ", rateDefinedFor='" + rateDefinedFor + '\'' +
                ", typeCode='" + typeCode + '\'' +
                ", transferTypeCode='" + transferTypeCode + '\'' +
                ", vehicleInformation=" + vehicleInformation +
                ", viaLocations=" + viaLocations +
                '}';
    }
}
