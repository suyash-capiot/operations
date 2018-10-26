
package com.coxandkings.travel.ext.model.finance.invoice;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "passengerName",
    "passengerID"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllPasseneger {

    @JsonProperty("passengerName")
    private String passengerName;
    @JsonProperty("passengerID")
    private String passengerID;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("passengerName")
    public String getPassengerName() {
        return passengerName;
    }

    @JsonProperty("passengerName")
    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    @JsonProperty("passengerID")
    public String getPassengerID() {
        return passengerID;
    }

    @JsonProperty("passengerID")
    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
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
