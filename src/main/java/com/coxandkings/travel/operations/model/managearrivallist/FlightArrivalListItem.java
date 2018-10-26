package com.coxandkings.travel.operations.model.managearrivallist;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Arrival_List_Flight")
public class FlightArrivalListItem extends BaseModel {


    @Column(name = "airlineName")
    private String airlineName;

    @Column(name = "fromSector")
    private String fromSector;

    @Column(name = "toSector")
    private String toSector;

    @Column(name = "passengerName")
    private String passengerName;

    @Column(name = "passengerType")
    private String passengerType;

    @Column(name = "supplierName")
    private String supplierName;

    @Column(name = "pcc")
    private String pccHapCredentials;
/*
    @Column(name = "credential")
    private String credential;

    @Column(name = "hap")
    private String hap;*/

    @Column(name = "pnr")
    private String pnr;

    @Column(name = "flightClass")
    private String flightClass;

    @Column(name = "rbd")
    private String rbd;

    @Column(name = "totalNumberOfPassengers")
    private Integer totalNumberOfPassengers;


    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getFromSector() {
        return fromSector;
    }

    public void setFromSector(String fromSector) {
        this.fromSector = fromSector;
    }

    public String getToSector() {
        return toSector;
    }

    public void setToSector(String toSector) {
        this.toSector = toSector;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPccHapCredentials() {
        return pccHapCredentials;
    }

    public void setPccHapCredentials(String pccHapCredentials) {
        this.pccHapCredentials = pccHapCredentials;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    public String getRbd() {
        return rbd;
    }

    public void setRbd(String rbd) {
        this.rbd = rbd;
    }

    public Integer getTotalNumberOfPassengers() {
        return totalNumberOfPassengers;
    }

    public void setTotalNumberOfPassengers(Integer totalNumberOfPassengers) {
        this.totalNumberOfPassengers = totalNumberOfPassengers;
    }
}
