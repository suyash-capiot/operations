package com.coxandkings.travel.operations.model.prepaymenttosupplier.loader;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "prePaymentType",
        "supplierPreferredPaymentType",
        "definePaymentByTravelCountry",
        "payableAmount",
        "travelCountries",
        "credentialNames"
})
public class PrePaymentSettlement {

    @JsonProperty("definePaymentByTravelCountry")
    private Boolean definePaymentByTravelCountry;
    @JsonProperty("prePaymentType")
    private String prePaymentType;
    @JsonProperty("supplierPreferredPaymentType")
    private String supplierPreferredPaymentType;
    @JsonProperty("payableAmount")
    private PayableAmount payableAmount;
    @JsonProperty("travelCountries")
    private List<Object> travelCountries = null;
    @JsonProperty("credentialNames")
    private List<Object> credentialNames = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("definePaymentByTravelCountry")
    public Boolean getDefinePaymentByTravelCountry() {
        return definePaymentByTravelCountry;
    }

    @JsonProperty("definePaymentByTravelCountry")
    public void setDefinePaymentByTravelCountry(Boolean definePaymentByTravelCountry) {
        this.definePaymentByTravelCountry = definePaymentByTravelCountry;
    }

    @JsonProperty("prePaymentType")
    public String getPrePaymentType() {
        return prePaymentType;
    }

    @JsonProperty("prePaymentType")
    public void setPrePaymentType(String prePaymentType) {
        this.prePaymentType = prePaymentType;
    }

    @JsonProperty("payableAmount")
    public PayableAmount getPayableAmount() {
        return payableAmount;
    }

    @JsonProperty("payableAmount")
    public void setPayableAmount(PayableAmount payableAmount) {
        this.payableAmount = payableAmount;
    }

    @JsonProperty("travelCountries")
    public List<Object> getTravelCountries() {
        return travelCountries;
    }

    @JsonProperty("travelCountries")
    public void setTravelCountries(List<Object> travelCountries) {
        this.travelCountries = travelCountries;
    }

    @JsonProperty("credentialNames")
    public List<Object> getCredentialNames() {
        return credentialNames;
    }

    @JsonProperty("credentialNames")
    public void setCredentialNames(List<Object> credentialNames) {
        this.credentialNames = credentialNames;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("supplierPreferredPaymentType")
    public String getSupplierPreferredPaymentType() {
        return supplierPreferredPaymentType;
    }

    @JsonProperty("supplierPreferredPaymentType")
    public void setSupplierPreferredPaymentType(String supplierPreferredPaymentType) {
        this.supplierPreferredPaymentType = supplierPreferredPaymentType;
    }
}