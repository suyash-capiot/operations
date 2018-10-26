
package com.coxandkings.travel.operations.resource.qcmanagement.client;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "flightTiming",
        "flightNumber",
        "cabinClass",
        "rbd"
})
public class BrmsClientFlightDetail {

    @JsonProperty("flightTiming")
    private String flightTiming;
    @JsonProperty("flightNumber")
    private Integer flightNumber;
    @JsonProperty("cabinClass")
    private String cabinClass;
    @JsonProperty("rbd")
    private String rbd;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("flightTiming")
    public String getFlightTiming() {
        return flightTiming;
    }

    @JsonProperty("flightTiming")
    public void setFlightTiming(String flightTiming) {
        this.flightTiming = flightTiming;
    }

    @JsonProperty("flightNumber")
    public Integer getFlightNumber() {
        return flightNumber;
    }

    @JsonProperty("flightNumber")
    public void setFlightNumber(Integer flightNumber) {
        this.flightNumber = flightNumber;
    }

    @JsonProperty("cabinClass")
    public String getCabinClass() {
        return cabinClass;
    }

    @JsonProperty("cabinClass")
    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    @JsonProperty("rbd")
    public String getRbd() {
        return rbd;
    }

    @JsonProperty("rbd")
    public void setRbd(String rbd) {
        this.rbd = rbd;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
