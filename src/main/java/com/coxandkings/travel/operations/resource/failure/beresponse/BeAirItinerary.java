
package com.coxandkings.travel.operations.resource.failure.beresponse;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "originDestinationOptions"
})
public class BeAirItinerary {

    @JsonProperty("originDestinationOptions")
    private List<BeOriginDestinationOption> originDestinationOptions = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("originDestinationOptions")
    public List<BeOriginDestinationOption> getOriginDestinationOptions() {
        return originDestinationOptions;
    }

    @JsonProperty("originDestinationOptions")
    public void setOriginDestinationOptions(List<BeOriginDestinationOption> originDestinationOptions) {
        this.originDestinationOptions = originDestinationOptions;
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
