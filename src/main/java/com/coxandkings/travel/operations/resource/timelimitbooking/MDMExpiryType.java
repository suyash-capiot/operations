package com.coxandkings.travel.operations.resource.timelimitbooking;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "expiryType",
        "shouldExpire"
})
public class MDMExpiryType {

    @JsonProperty("expiryType")
    private String expiryType;
    @JsonProperty("shouldExpire")
    private Boolean shouldExpire;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("expiryType")
    public String getExpiryType() {
        return expiryType;
    }

    @JsonProperty("expiryType")
    public void setExpiryType(String expiryType) {
        this.expiryType = expiryType;
    }

    @JsonProperty("shouldExpire")
    public Boolean getShouldExpire() {
        return shouldExpire;
    }

    @JsonProperty("shouldExpire")
    public void setShouldExpire(Boolean shouldExpire) {
        this.shouldExpire = shouldExpire;
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