package com.coxandkings.travel.ext.model.be;

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
public class OfferCodes {

    @JsonProperty("offerId")
    private String offerId;

    @JsonProperty("offerCodes")
    private List<String> offerCodes;

    @JsonProperty("amount")
    private BigDecimal amount;

    public OfferCodes(){

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
