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
        "balance",
        "_id"
})
public class Balance {

    @JsonProperty("numberOfDays")
    private int numberOfDays;
    @JsonProperty("settlementSchedule")
    private String settlementSchedule;
    @JsonProperty("balance")
    private int balance;
    @JsonProperty("_id")
    private String id;
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

    @JsonProperty("balance")
    public int getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(int balance) {
        this.balance = balance;
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