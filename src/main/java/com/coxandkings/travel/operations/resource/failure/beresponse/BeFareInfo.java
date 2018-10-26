
package com.coxandkings.travel.operations.resource.failure.beresponse;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "paxType",
        "fareBasisCode",
        "fareReference"
})
public class BeFareInfo {

    @JsonProperty("paxType")
    private String paxType;
    @JsonProperty("fareBasisCode")
    private String fareBasisCode;
    @JsonProperty("fareReference")
    private String fareReference;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("paxType")
    public String getPaxType() {
        return paxType;
    }

    @JsonProperty("paxType")
    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    @JsonProperty("fareBasisCode")
    public String getFareBasisCode() {
        return fareBasisCode;
    }

    @JsonProperty("fareBasisCode")
    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    @JsonProperty("fareReference")
    public String getFareReference() {
        return fareReference;
    }

    @JsonProperty("fareReference")
    public void setFareReference(String fareReference) {
        this.fareReference = fareReference;
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
