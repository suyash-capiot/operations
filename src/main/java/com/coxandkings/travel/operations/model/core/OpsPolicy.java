
package com.coxandkings.travel.operations.model.core;

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
        "policyCharges",
        "policyApplicability",
        "policyType"
})
public class OpsPolicy {

    @JsonProperty("policyCharges")
    private OpsPolicyCharges policyCharges;
    @JsonProperty("policyApplicability")
    private OpsPolicyApplicability policyApplicability;
    @JsonProperty("policyType")
    private String policyType;

    public OpsPolicyCharges getPolicyCharges() {
        return policyCharges;
    }

    public void setPolicyCharges(OpsPolicyCharges policyCharges) {
        this.policyCharges = policyCharges;
    }

    public OpsPolicyApplicability getPolicyApplicability() {
        return policyApplicability;
    }

    public void setPolicyApplicability(OpsPolicyApplicability policyApplicability) {
        this.policyApplicability = policyApplicability;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }
}
