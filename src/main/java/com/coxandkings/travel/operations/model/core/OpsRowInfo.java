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
public class OpsRowInfo implements Serializable {

    @JsonProperty("seatInfo")
    private List<OpsSeatInfo> seatInfo = new ArrayList<>();

    @JsonProperty("rowNumber")
    private String rowNumber;

    @JsonProperty("cabinType")
    private  String cabinType;

    public List<OpsSeatInfo> getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(List<OpsSeatInfo> seatInfo) {
        this.seatInfo = seatInfo;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getCabinType() {
        return cabinType;
    }

    public void setCabinType(String cabinType) {
        this.cabinType = cabinType;
    }

    public OpsRowInfo(List<OpsSeatInfo> seatInfo, String rowNumber, String cabinType) {
        this.seatInfo = seatInfo;
        this.rowNumber = rowNumber;
        this.cabinType = cabinType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsRowInfo that = (OpsRowInfo) o;
        return Objects.equals(seatInfo, that.seatInfo) &&
                Objects.equals(rowNumber, that.rowNumber) &&
                Objects.equals(cabinType, that.cabinType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(seatInfo, rowNumber, cabinType);
    }

    public OpsRowInfo() {
    }
}
