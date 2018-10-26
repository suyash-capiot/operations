
package com.coxandkings.travel.ext.model.finance.creditdebitnote;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "costHeader",
        "passengerType",
        "passengerQuantity",
        "currency",
        "amount",
        "totalAmount"
})
public class CostHeaderCharge {

    @JsonProperty("id")
    private String id;
    @JsonProperty("costHeader")
    private String costHeader;
    @JsonProperty("passengerType")
    private String passengerType;
    @JsonProperty("passengerQuantity")
    private Integer passengerQuantity;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("totalAmount")
    private Double totalAmount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("costHeader")
    public String getCostHeader() {
        return costHeader;
    }

    @JsonProperty("costHeader")
    public void setCostHeader(String costHeader) {
        this.costHeader = costHeader;
    }

    @JsonProperty("passengerType")
    public String getPassengerType() {
        return passengerType;
    }

    @JsonProperty("passengerType")
    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    @JsonProperty("passengerQuantity")
    public Integer getPassengerQuantity() {
        return passengerQuantity;
    }

    @JsonProperty("passengerQuantity")
    public void setPassengerQuantity(Integer passengerQuantity) {
        this.passengerQuantity = passengerQuantity;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("totalAmount")
    public Double getTotalAmount() {
        return totalAmount;
    }

    @JsonProperty("totalAmount")
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
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
