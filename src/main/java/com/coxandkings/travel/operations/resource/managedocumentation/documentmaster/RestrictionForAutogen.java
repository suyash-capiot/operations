package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cutOffMode",
    "applyAs",
    "cutOffPeriod"
})
public class RestrictionForAutogen {

    @JsonProperty("cutOffMode")
    private String cutOffMode;
    @JsonProperty("applyAs")
    private String applyAs;
    @JsonProperty("cutOffPeriod")
    private Integer cutOffPeriod;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("cutOffMode")
    public String getCutOffMode() {
        return cutOffMode;
    }

    @JsonProperty("cutOffMode")
    public void setCutOffMode(String cutOffMode) {
        this.cutOffMode = cutOffMode;
    }

    @JsonProperty("applyAs")
    public String getApplyAs() {
        return applyAs;
    }

    @JsonProperty("applyAs")
    public void setApplyAs(String applyAs) {
        this.applyAs = applyAs;
    }

    @JsonProperty("cutOffPeriod")
    public Integer getCutOffPeriod() {
        return cutOffPeriod;
    }

    @JsonProperty("cutOffPeriod")
    public void setCutOffPeriod(Integer cutOffPeriod) {
        this.cutOffPeriod = cutOffPeriod;
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
