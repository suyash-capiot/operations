package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CabinClass implements Serializable {

    @JsonProperty("rowInfo")
    private List<RowInfo> rowInfo = new ArrayList<>();

    @JsonProperty("endingRow")
    private String endingRow;

    @JsonProperty("upperDeckInd")
    private String upperDeckInd;

    @JsonProperty("startingRow")
    private String startingRow;

    @JsonProperty("rowInfo")
    public List<RowInfo> getRowInfo() {
        return rowInfo;
    }

    @JsonProperty("rowInfo")
    public void setRowInfo(List<RowInfo> rowInfo) {
        this.rowInfo = rowInfo;
    }

    @JsonProperty("endingRow")
    public String getEndingRow() {
        return endingRow;
    }

    @JsonProperty("endingRow")
    public void setEndingRow(String endingRow) {
        this.endingRow = endingRow;
    }

    @JsonProperty("upperDeckInd")
    public String getUpperDeckInd() {
        return upperDeckInd;
    }

    @JsonProperty("upperDeckInd")
    public void setUpperDeckInd(String upperDeckInd) {
        this.upperDeckInd = upperDeckInd;
    }

    @JsonProperty("startingRow")
    public String getStartingRow() {
        return startingRow;
    }

    @JsonProperty("startingRow")
    public void setStartingRow(String startingRow) {
        this.startingRow = startingRow;
    }

    public CabinClass(List<RowInfo> rowInfo, String endingRow, String upperDeckInd, String startingRow) {
        this.rowInfo = rowInfo;
        this.endingRow = endingRow;
        this.upperDeckInd = upperDeckInd;
        this.startingRow = startingRow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CabinClass that = (CabinClass) o;
        return Objects.equals(rowInfo, that.rowInfo) &&
                Objects.equals(endingRow, that.endingRow) &&
                Objects.equals(upperDeckInd, that.upperDeckInd) &&
                Objects.equals(startingRow, that.startingRow);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rowInfo, endingRow, upperDeckInd, startingRow);
    }

    public CabinClass() {
    }
}

