package com.coxandkings.travel.operations.model.prepaymenttosupplier.loader;

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
        "creditType",
        "_id",
        "prePaymentSettlement",
        "depositSettlement"
})
public class NoCreditSettlement {

    @JsonProperty("creditType")
    private String creditType;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("prePaymentSettlement")
    private PrePaymentSettlement prePaymentSettlement;
    @JsonProperty("depositSettlement")
    private DepositSettlement depositSettlement;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("creditType")
    public String getCreditType() {
        return creditType;
    }

    @JsonProperty("creditType")
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("prePaymentSettlement")
    public PrePaymentSettlement getPrePaymentSettlement() {
        return prePaymentSettlement;
    }

    @JsonProperty("prePaymentSettlement")
    public void setPrePaymentSettlement(PrePaymentSettlement prePaymentSettlement) {
        this.prePaymentSettlement = prePaymentSettlement;
    }

    @JsonProperty("depositSettlement")
    public DepositSettlement getDepositSettlement() {
        return depositSettlement;
    }

    @JsonProperty("depositSettlement")
    public void setDepositSettlement(DepositSettlement depositSettlement) {
        this.depositSettlement = depositSettlement;
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