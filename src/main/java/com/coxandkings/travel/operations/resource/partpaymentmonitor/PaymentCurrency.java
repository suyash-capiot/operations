
package com.coxandkings.travel.operations.resource.partpaymentmonitor;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currencyName"
})
public class PaymentCurrency {

    @JsonProperty("currencyName")
    private String currencyName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("currencyName")
    public String getCurrencyName() {
        return currencyName;
    }

    @JsonProperty("currencyName")
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
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
