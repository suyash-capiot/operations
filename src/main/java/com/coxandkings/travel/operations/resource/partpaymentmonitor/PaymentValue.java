
package com.coxandkings.travel.operations.resource.partpaymentmonitor;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currencyAmount",
        "priceComponent",
        "applyValue",
        "mode",
        "currency"
})
public class PaymentValue {

    @JsonProperty("currencyAmount")
    private Integer currencyAmount;
    @JsonProperty("priceComponent")
    private String priceComponent;
    @JsonProperty("applyValue")
    private String applyValue;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("currency")
    private PaymentCurrency currency;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("currencyAmount")
    public Integer getCurrencyAmount() {
        return currencyAmount;
    }

    @JsonProperty("currencyAmount")
    public void setCurrencyAmount(Integer currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    @JsonProperty("priceComponent")
    public String getPriceComponent() {
        return priceComponent;
    }

    @JsonProperty("priceComponent")
    public void setPriceComponent(String priceComponent) {
        this.priceComponent = priceComponent;
    }

    @JsonProperty("applyValue")
    public String getApplyValue() {
        return applyValue;
    }

    @JsonProperty("applyValue")
    public void setApplyValue(String applyValue) {
        this.applyValue = applyValue;
    }

    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonProperty("currency")
    public PaymentCurrency getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(PaymentCurrency currency) {
        this.currency = currency;
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
