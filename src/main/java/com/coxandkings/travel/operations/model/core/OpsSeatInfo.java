package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsSeatInfo implements Serializable {

    @JsonProperty("availableInd")
    private  String availableInd;

    @JsonProperty("features")
    private List<OpsFeatures> features = new ArrayList<>();

    @JsonProperty("seatPreference")
    private String seatPreference;

    @JsonProperty("occupiedInd")
    private String occupiedInd;

    @JsonProperty("gallyInd")
    private String gallyInd;

    @JsonProperty("serviceFees")
    private List<OpsServiceFees> serviceFees = new ArrayList<>();

    @JsonProperty("bulkheadInd")
    private String bulkheadInd;

    @JsonProperty("seatNumber")
    private String seatNumber;

    @JsonProperty("exitRowInd")
    private String exitRowInd;

    @JsonProperty("seatSequenceNumber")
    private String seatSequenceNumber;

    public String getAvailableInd() {
        return availableInd;
    }

    public void setAvailableInd(String availableInd) {
        this.availableInd = availableInd;
    }

    public List<OpsFeatures> getFeatures() {
        return features;
    }

    public void setFeatures(List<OpsFeatures> features) {
        this.features = features;
    }

    public String getSeatPreference() {
        return seatPreference;
    }

    public void setSeatPreference(String seatPreference) {
        this.seatPreference = seatPreference;
    }

    public String getOccupiedInd() {
        return occupiedInd;
    }

    public void setOccupiedInd(String occupiedInd) {
        this.occupiedInd = occupiedInd;
    }

    public String getGallyInd() {
        return gallyInd;
    }

    public void setGallyInd(String gallyInd) {
        this.gallyInd = gallyInd;
    }

    public List<OpsServiceFees> getServiceFees() {
        return serviceFees;
    }

    public void setServiceFees(List<OpsServiceFees> serviceFees) {
        this.serviceFees = serviceFees;
    }

    public String getBulkheadInd() {
        return bulkheadInd;
    }

    public void setBulkheadInd(String bulkheadInd) {
        this.bulkheadInd = bulkheadInd;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getExitRowInd() {
        return exitRowInd;
    }

    public void setExitRowInd(String exitRowInd) {
        this.exitRowInd = exitRowInd;
    }

    public String getSeatSequenceNumber() {
        return seatSequenceNumber;
    }

    public void setSeatSequenceNumber(String seatSequenceNumber) {
        this.seatSequenceNumber = seatSequenceNumber;
    }

    public OpsSeatInfo(String availableInd, List<OpsFeatures> features, String seatPreference, String occupiedInd, String gallyInd, List<OpsServiceFees> serviceFees, String bulkheadInd, String seatNumber, String exitRowInd, String seatSequenceNumber) {
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

    public OpsSeatInfo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsSeatInfo that = (OpsSeatInfo) o;
        return Objects.equals(availableInd, that.availableInd) &&
                Objects.equals(features, that.features) &&
                Objects.equals(seatPreference, that.seatPreference) &&
                Objects.equals(occupiedInd, that.occupiedInd) &&
                Objects.equals(gallyInd, that.gallyInd) &&
                Objects.equals(serviceFees, that.serviceFees) &&
                Objects.equals(bulkheadInd, that.bulkheadInd) &&
                Objects.equals(seatNumber, that.seatNumber) &&
                Objects.equals(exitRowInd, that.exitRowInd) &&
                Objects.equals(seatSequenceNumber, that.seatSequenceNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(availableInd, features, seatPreference, occupiedInd, gallyInd, serviceFees, bulkheadInd, seatNumber, exitRowInd, seatSequenceNumber);
    }
}
