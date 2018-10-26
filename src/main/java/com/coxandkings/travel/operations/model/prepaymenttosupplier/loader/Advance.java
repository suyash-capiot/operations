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
        "numberOfDays",
        "settlementSchedule",
        "advance"
})
public class Advance {

    @JsonProperty("numberOfDays")
    private int numberOfDays;
    @JsonProperty("settlementSchedule")
    private String settlementSchedule;
    @JsonProperty("advance")
    private int advance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("numberOfDays")
    public int getNumberOfDays() {
        return numberOfDays;
    }

    @JsonProperty("numberOfDays")
    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    @JsonProperty("settlementSchedule")
    public String getSettlementSchedule() {
        return settlementSchedule;
    }

    @JsonProperty("settlementSchedule")
    public void setSettlementSchedule(String settlementSchedule) {
        this.settlementSchedule = settlementSchedule;
    }

    @JsonProperty("advance")
    public int getAdvance() {
        return advance;
    }

    @JsonProperty("advance")
    public void setAdvance(int advance) {
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