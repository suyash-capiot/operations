
package com.coxandkings.travel.ext.model.be;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "policyCharges",
    "policyApplicability",
    "policyType"
})
public class Policy implements Serializable {

    @JsonProperty("policyCharges")
    private PolicyCharges policyCharges;
    @JsonProperty("policyApplicability")
    private PolicyApplicability policyApplicability;
    @JsonProperty("policyType")
    private String policyType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("policyCharges")
    public PolicyCharges getPolicyCharges() {
        return policyCharges;
    }

    @JsonProperty("policyCharges")
    public void setPolicyCharges(PolicyCharges policyCharges) {
        this.policyCharges = policyCharges;
    }

    @JsonProperty("policyApplicability")
    public PolicyApplicability getPolicyApplicability() {
        return policyApplicability;
    }

    @JsonProperty("policyApplicability")
    public void setPolicyApplicability(PolicyApplicability policyApplicability) {
        this.policyApplicability = policyApplicability;
    }

    @JsonProperty("policyType")
    public String getPolicyType() {
        return policyType;
    }

    @JsonProperty("policyType")
    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Policy)) return false;
        Policy policy = (Policy) o;
        return Objects.equals(getPolicyCharges(), policy.getPolicyCharges()) &&
                Objects.equals(getPolicyApplicability(), policy.getPolicyApplicability()) &&
                Objects.equals(getPolicyType(), policy.getPolicyType()) &&
                Objects.equals(getAdditionalProperties(), policy.getAdditionalProperties());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getPolicyCharges(), getPolicyApplicability(), getPolicyType(), getAdditionalProperties());
    }
}
