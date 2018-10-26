
package com.coxandkings.travel.ext.model.be;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fareRuleInfo",
    "fareRule"
})
public class FareInfo {

    @JsonProperty("fareRuleInfo")
    private FareRuleInfo fareRuleInfo;
    @JsonProperty("fareRule")
    private FareRule fareRule = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("fareRuleInfo")
    public FareRuleInfo getFareRuleInfo() {
        return fareRuleInfo;
    }

    @JsonProperty("fareRuleInfo")
    public void setFareRuleInfo(FareRuleInfo fareRuleInfo) {
        this.fareRuleInfo = fareRuleInfo;
    }

    public FareRule getFareRule() {
        return fareRule;
    }

    public void setFareRule(FareRule fareRule) {
        this.fareRule = fareRule;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
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
