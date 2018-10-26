package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaySummary {
    @JsonProperty("travelType")
    private String travelType;
    @JsonProperty("travelTypeDetails")
    private List<OpsHolidaysTravelTypeDetails> travelTypeDetails;

    public OpsHolidaySummary() {
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public List<OpsHolidaysTravelTypeDetails> getTravelTypeDetails() {
        return travelTypeDetails;
    }

    public void setTravelTypeDetails(List<OpsHolidaysTravelTypeDetails> travelTypeDetails) {
        this.travelTypeDetails = travelTypeDetails;
    }
}
