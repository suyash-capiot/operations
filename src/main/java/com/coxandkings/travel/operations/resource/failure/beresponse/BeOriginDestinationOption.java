
package com.coxandkings.travel.operations.resource.failure.beresponse;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "flightSegment"
})
public class BeOriginDestinationOption {

    @JsonProperty("flightSegment")
    private List<BeFlightSegment> flightSegment = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("flightSegment")
    public List<BeFlightSegment> getFlightSegment() {
        return flightSegment;
    }

    @JsonProperty("flightSegment")
    public void setFlightSegment(List<BeFlightSegment> flightSegment) {
        this.flightSegment = flightSegment;
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
