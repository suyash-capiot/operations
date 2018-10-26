package com.coxandkings.travel.operations.model.prepaymenttosupplier.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "balance",
        "advance"
})
public class PayableAmount {

    @JsonProperty("balance")
    private List<Balance> balance = null;
    @JsonProperty("advance")
    private Advance advance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("balance")
    public List<Balance> getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(List<Balance> balance) {
        this.balance = balance;
    }

    @JsonProperty("advance")
    public Advance getAdvance() {
        return advance;
    }

    @JsonProperty("advance")
    public void setAdvance(Advance advance) {
        this.advance = advance;
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
