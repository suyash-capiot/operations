
package com.coxandkings.travel.operations.resource.failure.beresponse;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "amount",
        "baseFare",
        "fees",
        "taxes",
        "currencyCode"
})
public class BeItinTotalFare {

    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("baseFare")
    private BeBaseFare baseFare;
    @JsonProperty("fees")
    private BeFees fees;
    @JsonProperty("taxes")
    private BeTaxes taxes;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @JsonProperty("baseFare")
    public BeBaseFare getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(BeBaseFare baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("fees")
    public BeFees getFees() {
        return fees;
    }

    @JsonProperty("fees")
    public void setFees(BeFees fees) {
        this.fees = fees;
    }

    @JsonProperty("taxes")
    public BeTaxes getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(BeTaxes taxes) {
        this.taxes = taxes;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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
