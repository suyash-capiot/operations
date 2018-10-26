package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeatInfo implements Serializable {

    @JsonProperty("availableInd")
    private  String availableInd;

    @JsonProperty("features")
    private List<Features> features = new ArrayList<>();

    @JsonProperty("seatPreference")
    private String seatPreference;

    @JsonProperty("occupiedInd")
    private String occupiedInd;

    @JsonProperty("gallyInd")
    private String gallyInd;

    @JsonProperty("serviceFees")
    private List<ServiceFees> serviceFees = new ArrayList<>();

    @JsonProperty("bulkheadInd")
    private String bulkheadInd;

    @JsonProperty("seatNumber")
    private String seatNumber;

    @JsonProperty("exitRowInd")
    private String exitRowInd;

    @JsonProperty("seatSequenceNumber")
    private String seatSequenceNumber;


    @JsonProperty("availableInd")
    public String getAvailableInd() {
        return availableInd;
    }

    @JsonProperty("availableInd")
    public void setAvailableInd(String availableInd) {
        this.availableInd = availableInd;
    }

    @JsonProperty("features")
    public List<Features> getFeatures() {
        return features;
    }

    @JsonProperty("features")
    public void setFeatures(List<Features> features) {
        this.features = features;
    }

    @JsonProperty("seatPreference")
    public String getSeatPreference() {
        return seatPreference;
    }

    @JsonProperty("seatPreference")
    public void setSeatPreference(String seatPreference) {
        this.seatPreference = seatPreference;
    }

    @JsonProperty("occupiedInd")
    public String getOccupiedInd() {
        return occupiedInd;
    }

    @JsonProperty("occupiedInd")
    public void setOccupiedInd(String occupiedInd) {
        this.occupiedInd = occupiedInd;
    }

    @JsonProperty("gallyInd")
    public String getGallyInd() {
        return gallyInd;
    }

    @JsonProperty("gallyInd")
    public void setGallyInd(String gallyInd) {
        this.gallyInd = gallyInd;
    }

    @JsonProperty("serviceFees")
    public List<ServiceFees> getServiceFees() {
        return serviceFees;
    }

    @JsonProperty("serviceFees")
    public void setServiceFees(List<ServiceFees> serviceFees) {
        this.serviceFees = serviceFees;
    }

    @JsonProperty("bulkheadInd")
    public String getBulkheadInd() {
        return bulkheadInd;
    }

    @JsonProperty("bulkheadInd")
    public void setBulkheadInd(String bulkheadInd) {
        this.bulkheadInd = bulkheadInd;
    }

    @JsonProperty("seatNumber")
    public String getSeatNumber() {
        return seatNumber;
    }

    @JsonProperty("seatNumber")
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @JsonProperty("exitRowInd")
    public String getExitRowInd() {
        return exitRowInd;
    }

    @JsonProperty("exitRowInd")
    public void setExitRowInd(String exitRowInd) {
        this.exitRowInd = exitRowInd;
    }

    @JsonProperty("seatSequenceNumber")
    public String getSeatSequenceNumber() {
        return seatSequenceNumber;
    }

    @JsonProperty("seatSequenceNumber")
    public void setSeatSequenceNumber(String seatSequenceNumber) {
        this.seatSequenceNumber = seatSequenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatInfo seatInfo = (SeatInfo) o;
        return Objects.equals(availableInd, seatInfo.availableInd) &&
                Objects.equals(features, seatInfo.features) &&
                Objects.equals(seatPreference, seatInfo.seatPreference) &&
                Objects.equals(occupiedInd, seatInfo.occupiedInd) &&
                Objects.equals(gallyInd, seatInfo.gallyInd) &&
                Objects.equals(serviceFees, seatInfo.serviceFees) &&
                Objects.equals(bulkheadInd, seatInfo.bulkheadInd) &&
                Objects.equals(seatNumber, seatInfo.seatNumber) &&
                Objects.equals(exitRowInd, seatInfo.exitRowInd) &&
                Objects.equals(seatSequenceNumber, seatInfo.seatSequenceNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(availableInd, features, seatPreference, occupiedInd, gallyInd, serviceFees, bulkheadInd, seatNumber, exitRowInd, seatSequenceNumber);
    }

    public SeatInfo(String availableInd, List<Features> features, String seatPreference, String occupiedInd, String gallyInd, List<ServiceFees> serviceFees, String bulkheadInd, String seatNumber, String exitRowInd, String seatSequenceNumber) {
        this.availableInd = availableInd;
        this.features = features;
        this.seatPreference = seatPreference;
        this.occupiedInd = occupiedInd;
        this.gallyInd = gallyInd;
        this.serviceFees = serviceFees;
        this.bulkheadInd = bulkheadInd;
        this.seatNumber = seatNumber;
        this.exitRowInd = exitRowInd;
        this.seatSequenceNumber = seatSequenceNumber;
    }

    public SeatInfo() {
    }
}
