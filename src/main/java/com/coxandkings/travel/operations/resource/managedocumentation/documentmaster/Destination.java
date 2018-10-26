package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "destinationId",
    "destinationName"
})
public class Destination {

    @JsonProperty("destinationId")
    private String destinationId;
    @JsonProperty("destinationName")
    private String destinationName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("destinationId")
    public String getDestinationId() {
        return destinationId;
    }

    @JsonProperty("destinationId")
    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    @JsonProperty("destinationName")
    public String getDestinationName() {
        return destinationName;
    }

    @JsonProperty("destinationName")
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
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
