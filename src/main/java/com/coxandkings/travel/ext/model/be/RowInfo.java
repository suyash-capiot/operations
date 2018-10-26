package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RowInfo implements Serializable {

    @JsonProperty("seatInfo")
    private List<SeatInfo> seatInfo = new ArrayList<>();

    @JsonProperty("rowNumber")
    private String rowNumber;

    @JsonProperty("cabinType")
    private  String cabinType;

    @JsonProperty("seatInfo")
    public List<SeatInfo> getSeatInfo() {
        return seatInfo;
    }

    @JsonProperty("seatInfo")
    public void setSeatInfo(List<SeatInfo> seatInfo) {
        this.seatInfo = seatInfo;
    }

    @JsonProperty("rowNumber")
    public String getRowNumber() {
        return rowNumber;
    }

    @JsonProperty("rowNumber")
    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    @JsonProperty("cabinType")
    public String getCabinType() {
        return cabinType;
    }

    @JsonProperty("cabinType")
    public void setCabinType(String cabinType) {
        this.cabinType = cabinType;
    }

    public RowInfo(List<SeatInfo> seatInfo, String rowNumber, String cabinType) {
        this.seatInfo = seatInfo;
        this.rowNumber = rowNumber;
        this.cabinType = cabinType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RowInfo rowInfo = (RowInfo) o;
        return Objects.equals(seatInfo, rowInfo.seatInfo) &&
                Objects.equals(rowNumber, rowInfo.rowNumber) &&
                Objects.equals(cabinType, rowInfo.cabinType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(seatInfo, rowNumber, cabinType);
    }

    public RowInfo() {
    }
}
