package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "criteriaName",
    "approvalRequired"
})
public class Other {

    @JsonProperty("criteriaName")
    private String criteriaName;
    @JsonProperty("approvalRequired")
    private Boolean approvalRequired;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("criteriaName")
    public String getCriteriaName() {
        return criteriaName;
    }

    @JsonProperty("criteriaName")
    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    @JsonProperty("approvalRequired")
    public Boolean getApprovalRequired() {
        return approvalRequired;
    }

    @JsonProperty("approvalRequired")
    public void setApprovalRequired(Boolean approvalRequired) {
        this.approvalRequired = approvalRequired;
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
