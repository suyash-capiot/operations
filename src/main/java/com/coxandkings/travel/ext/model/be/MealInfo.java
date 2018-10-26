
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mealID",
    "mealName"
})
public class MealInfo implements Serializable
{

    @JsonProperty("mealID")
    private String mealID;
    @JsonProperty("mealName")
    private String mealName;
    private final static long serialVersionUID = -3603851585599294344L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MealInfo() {
    }

    /**
     * 
     * @param mealID
     * @param mealName
     */
    public MealInfo(String mealID, String mealName) {
        super();
        this.mealID = mealID;
        this.mealName = mealName;
    }

    @JsonProperty("mealID")
    public String getMealID() {
        return mealID;
    }

    @JsonProperty("mealID")
    public void setMealID(String mealID) {
        this.mealID = mealID;
    }

    @JsonProperty("mealName")
    public String getMealName() {
        return mealName;
    }

    @JsonProperty("mealName")
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MealInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mealID");
        sb.append('=');
        sb.append(((this.mealID == null)?"<null>":this.mealID));
        sb.append(',');
        sb.append("mealName");
        sb.append('=');
        sb.append(((this.mealName == null)?"<null>":this.mealName));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.mealName == null)? 0 :this.mealName.hashCode()));
        result = ((result* 31)+((this.mealID == null)? 0 :this.mealID.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MealInfo) == false) {
            return false;
        }
        MealInfo rhs = ((MealInfo) other);
        return (((this.mealName == rhs.mealName)||((this.mealName!= null)&&this.mealName.equals(rhs.mealName)))&&((this.mealID == rhs.mealID)||((this.mealID!= null)&&this.mealID.equals(rhs.mealID))));
    }

}
