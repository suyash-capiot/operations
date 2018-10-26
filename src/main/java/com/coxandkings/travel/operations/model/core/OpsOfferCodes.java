package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "offerId",
        "offerCodes",
        "amount"
})
public class OpsOfferCodes {

    @JsonProperty("offerId")
    private String offerId;

    @JsonProperty("offerCodes")
    private List<String> offerCodes;

    @JsonProperty("amount")
    private BigDecimal amount;

    public OpsOfferCodes(){

    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public List<String> getOfferCodes() {
        return offerCodes;
    }

    public void setOfferCodes(List<String> offerCodes) {
        this.offerCodes = offerCodes;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
