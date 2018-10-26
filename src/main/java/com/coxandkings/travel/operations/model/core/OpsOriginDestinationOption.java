package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsOriginDestinationOption implements Serializable {
    @JsonProperty("flightSegment")
    private List<OpsFlightSegment> flightSegment = new ArrayList<OpsFlightSegment>();
    private final static long serialVersionUID = -2042687200987952718L;

    public OpsOriginDestinationOption() {
    }

    public OpsOriginDestinationOption(List<OpsFlightSegment> flightSegment) {
        this.flightSegment = flightSegment;
    }

//    public OpsOriginDestinationOption(OriginDestinationOption originDestinationOption) {
//        this.flightSegment = originDestinationOption.getFlightSegment().stream().map(flightSegment1 -> new OpsFlightSegment(flightSegment1)).collect(Collectors.toList());
//    }

    public List<OpsFlightSegment> getFlightSegment() {
        return flightSegment;
    }

    public void setFlightSegment(List<OpsFlightSegment> flightSegment) {
        this.flightSegment = flightSegment;
    }
}
