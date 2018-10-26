package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeatMap implements Serializable {


    @JsonProperty("cabinClass")
    private List<CabinClass> cabinClass = new ArrayList<>();

    @JsonProperty("flightRefNumberRPHList")
    private String flightRefNumberRPHList;

    @JsonProperty("flightNumber")
    private String flightNumber;


    @JsonProperty("cabinClass")
    public List<CabinClass> getCabinClass() {
        return cabinClass;
    }

    @JsonProperty("cabinClass")
    public void setCabinClass(List<CabinClass> cabinClass) {
        this.cabinClass = cabinClass;
    }

    @JsonProperty("flightRefNumberRPHList")
    public String getFlightRefNumberRPHList() {
        return flightRefNumberRPHList;
    }

    @JsonProperty("flightRefNumberRPHList")
    public void setFlightRefNumberRPHList(String flightRefNumberRPHList) {
        this.flightRefNumberRPHList = flightRefNumberRPHList;
    }

    @JsonProperty("flightNumber")
    public String getFlightNumber() {
        return flightNumber;
    }

    @JsonProperty("flightNumber")
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public SeatMap(List<CabinClass> cabinClass, String flightRefNumberRPHList, String flightNumber) {
        this.cabinClass = cabinClass;
        this.flightRefNumberRPHList = flightRefNumberRPHList;
        this.flightNumber = flightNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatMap seatMap = (SeatMap) o;
        return Objects.equals(cabinClass, seatMap.cabinClass) &&
                Objects.equals(flightRefNumberRPHList, seatMap.flightRefNumberRPHList) &&
                Objects.equals(flightNumber, seatMap.flightNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cabinClass, flightRefNumberRPHList, flightNumber);
    }

    public SeatMap() {
    }
}
