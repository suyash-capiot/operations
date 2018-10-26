package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsSpecialRequest {

    @JsonProperty("specialRequestInfo")
    private List<OpsSpecialRequestInfo> specialRequestInfo;

    @JsonProperty("mealRequestInfo")
    private List<OpsSpecialRequestInfo> mealRequestInfo;

    public OpsSpecialRequest() {
    }

    public List<OpsSpecialRequestInfo> getSpecialRequestInfo() {
        return specialRequestInfo;
    }

    public void setSpecialRequestInfo(List<OpsSpecialRequestInfo> specialRequestInfo) {
        this.specialRequestInfo = specialRequestInfo;
    }

    public List<OpsSpecialRequestInfo> getMealRequestInfo() {
        return mealRequestInfo;
    }

    public void setMealRequestInfo(List<OpsSpecialRequestInfo> mealRequestInfo) {
        this.mealRequestInfo = mealRequestInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsSpecialRequest that = (OpsSpecialRequest) o;
        return Objects.equals(specialRequestInfo, that.specialRequestInfo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(specialRequestInfo);
    }
}
