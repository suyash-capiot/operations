
package com.coxandkings.travel.operations.resource.failure.beresponse;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "airlineCode",
        "flightNumber"
})
public class BeMarketingAirline {

    @JsonProperty("airlineCode")
    private String airlineCode;
    @JsonProperty("flightNumber")
    private String flightNumber;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("airlineCode")
    public String getAirlineCode() {
        return airlineCode;
    }

    @JsonProperty("airlineCode")
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    @JsonProperty("flightNumber")
    public String getFlightNumber() {
        return flightNumber;
    }

    @JsonProperty("flightNumber")
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
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
