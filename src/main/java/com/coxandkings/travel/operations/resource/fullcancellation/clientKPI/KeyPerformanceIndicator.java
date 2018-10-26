
package com.coxandkings.travel.operations.resource.fullcancellation.clientKPI;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "functionalRole",
    "roleName",
    "effectiveFrom",
    "effectiveTo"
})
public class KeyPerformanceIndicator {

    @JsonProperty("functionalRole")
    private String functionalRole;
    @JsonProperty("roleName")
    private String roleName;
    @JsonProperty("effectiveFrom")
    private String effectiveFrom;
    @JsonProperty("effectiveTo")
    private String effectiveTo;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("functionalRole")
    public String getFunctionalRole() {
        return functionalRole;
    }

    @JsonProperty("functionalRole")
    public void setFunctionalRole(String functionalRole) {
        this.functionalRole = functionalRole;
    }

    @JsonProperty("roleName")
    public String getRoleName() {
        return roleName;
    }

    @JsonProperty("roleName")
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @JsonProperty("effectiveFrom")
    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    @JsonProperty("effectiveFrom")
    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    @JsonProperty("effectiveTo")
    public String getEffectiveTo() {
        return effectiveTo;
    }

    @JsonProperty("effectiveTo")
    public void setEffectiveTo(String effectiveTo) {
        this.effectiveTo = effectiveTo;
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
