package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaysTourDetails {
    @JsonProperty("title")
    private String title;
    @JsonProperty("noOfDays")
    private String noOfDays;
    @JsonProperty("noOfNights")
    private String noOfNights;
    @JsonProperty("destination")
    private String destination;

    public OpsHolidaysTourDetails() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getNoOfNights() {
        return noOfNights;
    }

    public void setNoOfNights(String noOfNights) {
        this.noOfNights = noOfNights;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
