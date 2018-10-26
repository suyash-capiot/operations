package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "criteriaName",
    "modeOfPayment",
    "paymentRealization",
    "approvalRequired"
})
public class Payment {

    @JsonProperty("criteriaName")
    private String criteriaName;
    @JsonProperty("modeOfPayment")
    private String modeOfPayment;
    @JsonProperty("paymentRealization")
    private Boolean paymentRealization;
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

    @JsonProperty("modeOfPayment")
    public String getModeOfPayment() {
        return modeOfPayment;
    }

    @JsonProperty("modeOfPayment")
    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    @JsonProperty("paymentRealization")
    public Boolean getPaymentRealization() {
        return paymentRealization;
    }

    @JsonProperty("paymentRealization")
    public void setPaymentRealization(Boolean paymentRealization) {
        this.paymentRealization = paymentRealization;
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
