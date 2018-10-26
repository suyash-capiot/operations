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
        "settlementDueMonth",
        "settleDueDay",
        "fortnight",
        "_id"
})
public class Fortnightly {

    @JsonProperty("settlementDueMonth")
    private String settlementDueMonth;
    @JsonProperty("settleDueDay")
    private int settleDueDay;
    @JsonProperty("fortnight")
    private String fortnight;
    @JsonProperty("_id")
    private String id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("settlementDueMonth")
    public String getSettlementDueMonth() {
        return settlementDueMonth;
    }

    @JsonProperty("settlementDueMonth")
    public void setSettlementDueMonth(String settlementDueMonth) {
        this.settlementDueMonth = settlementDueMonth;
    }

    @JsonProperty("settleDueDay")
    public int getSettleDueDay() {
        return settleDueDay;
    }

    @JsonProperty("settleDueDay")
    public void setSettleDueDay(int settleDueDay) {
        this.settleDueDay = settleDueDay;
    }

    @JsonProperty("fortnight")
    public String getFortnight() {
        return fortnight;
    }

    @JsonProperty("fortnight")
    public void setFortnight(String fortnight) {
        this.fortnight = fortnight;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
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