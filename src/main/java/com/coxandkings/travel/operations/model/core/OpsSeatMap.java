package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsSeatMap implements Serializable {

    @JsonProperty("cabinClass")
    private List<OpsCabinClass> cabinClass = new ArrayList<>();

    @JsonProperty("flightRefNumberRPHList")
    private String flightRefNumberRPHList;

    @JsonProperty("flightNumber")
    private String flightNumber;

    public List<OpsCabinClass> getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(List<OpsCabinClass> cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getFlightRefNumberRPHList() {
        return flightRefNumberRPHList;
    }

    public void setFlightRefNumberRPHList(String flightRefNumberRPHList) {
        this.flightRefNumberRPHList = flightRefNumberRPHList;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public OpsSeatMap(List<OpsCabinClass> cabinClass, String flightRefNumberRPHList, String flightNumber) {
        this.cabinClass = cabinClass;
        this.flightRefNumberRPHList = flightRefNumberRPHList;
        this.flightNumber = flightNumber;
    }

    public OpsSeatMap() {
    }
}
