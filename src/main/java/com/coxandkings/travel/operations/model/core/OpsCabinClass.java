package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OpsCabinClass implements Serializable {

    @JsonProperty("rowInfo")
    private List<OpsRowInfo> rowInfo = new ArrayList<>();

    @JsonProperty("endingRow")
    private String endingRow;

    @JsonProperty("upperDeckInd")
    private String upperDeckInd;

    @JsonProperty("startingRow")
    private String startingRow;

    public List<OpsRowInfo> getRowInfo() {
        return rowInfo;
    }

    public void setRowInfo(List<OpsRowInfo> rowInfo) {
        this.rowInfo = rowInfo;
    }

    public String getEndingRow() {
        return endingRow;
    }

    public void setEndingRow(String endingRow) {
        this.endingRow = endingRow;
    }

    public String getUpperDeckInd() {
        return upperDeckInd;
    }

    public void setUpperDeckInd(String upperDeckInd) {
        this.upperDeckInd = upperDeckInd;
    }

    public String getStartingRow() {
        return startingRow;
    }

    public void setStartingRow(String startingRow) {
        this.startingRow = startingRow;
    }

    public OpsCabinClass(List<OpsRowInfo> rowInfo, String endingRow, String upperDeckInd, String startingRow) {
        this.rowInfo = rowInfo;
        this.endingRow = endingRow;
        this.upperDeckInd = upperDeckInd;
        this.startingRow = startingRow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsCabinClass that = (OpsCabinClass) o;
        return Objects.equals(rowInfo, that.rowInfo) &&
                Objects.equals(endingRow, that.endingRow) &&
                Objects.equals(upperDeckInd, that.upperDeckInd) &&
                Objects.equals(startingRow, that.startingRow);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rowInfo, endingRow, upperDeckInd, startingRow);
    }

    public OpsCabinClass() {
    }
}
