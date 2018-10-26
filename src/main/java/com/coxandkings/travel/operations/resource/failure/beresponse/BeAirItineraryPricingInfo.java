
package com.coxandkings.travel.operations.resource.failure.beresponse;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "itinTotalFare",
        "paxTypeFares"
})
public class BeAirItineraryPricingInfo {

    @JsonProperty("itinTotalFare")
    private BeItinTotalFare itinTotalFare;
    @JsonProperty("paxTypeFares")
    private List<BePaxTypeFare> paxTypeFares = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("itinTotalFare")
    public BeItinTotalFare getItinTotalFare() {
        return itinTotalFare;
    }

    @JsonProperty("itinTotalFare")
    public void setItinTotalFare(BeItinTotalFare itinTotalFare) {
        this.itinTotalFare = itinTotalFare;
    }

    @JsonProperty("paxTypeFares")
    public List<BePaxTypeFare> getPaxTypeFares() {
        return paxTypeFares;
    }

    @JsonProperty("paxTypeFares")
    public void setPaxTypeFares(List<BePaxTypeFare> paxTypeFares) {
        this.paxTypeFares = paxTypeFares;
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
