package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsMealInfo implements Serializable {

    @JsonProperty("mealID")
    private String mealID;

    @JsonProperty("mealName")
    private String mealName;
    private final static long serialVersionUID = -3099217277435052307L;

    public OpsMealInfo() {
    }

    public OpsMealInfo(String mealID, String mealName) {
        this.mealID = mealID;
        this.mealName = mealName;
    }

//    public OpsMealInfo(MealInfo mealInfo) {
//        this.mealID = mealInfo.getMealID();
//        this.mealName = mealInfo.getMealName();
//    }

    public String getMealID() {
        return mealID;
    }

    public void setMealID(String mealID) {
        this.mealID = mealID;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsMealInfo that = (OpsMealInfo) o;
        return Objects.equals(mealID, that.mealID) &&
                Objects.equals(mealName, that.mealName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mealID, mealName);
    }
}
