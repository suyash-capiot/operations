package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedemptionConstruct {

    @JsonProperty("redemptionId")
    private String redemptionId;

    @JsonProperty("allPaxSameAsOfferBooking")
    private boolean allPaxSameAsOfferBooking;

    @JsonProperty("userIdSameAsOfferBooking")
    private boolean userIdSameAsOfferBooking;

    @JsonProperty("cardSameAsOfferBooking")
    private boolean cardSameAsOfferBooking;

    @JsonProperty("minOnePaxShouldMatchOfferBooking")
    private boolean minOnePaxShouldMatchOfferBooking;

    public RedemptionConstruct(){

    }

    public String getRedemptionId() {
        return redemptionId;
    }

    public void setRedemptionId(String redemptionId) {
        this.redemptionId = redemptionId;
    }

    public boolean isAllPaxSameAsOfferBooking() {
        return allPaxSameAsOfferBooking;
    }

    public void setAllPaxSameAsOfferBooking(boolean allPaxSameAsOfferBooking) {
        this.allPaxSameAsOfferBooking = allPaxSameAsOfferBooking;
    }

    public boolean isUserIdSameAsOfferBooking() {
        return userIdSameAsOfferBooking;
    }

    public void setUserIdSameAsOfferBooking(boolean userIdSameAsOfferBooking) {
        this.userIdSameAsOfferBooking = userIdSameAsOfferBooking;
    }

    public boolean isCardSameAsOfferBooking() {
        return cardSameAsOfferBooking;
    }

    public void setCardSameAsOfferBooking(boolean cardSameAsOfferBooking) {
        this.cardSameAsOfferBooking = cardSameAsOfferBooking;
    }

    public boolean isMinOnePaxShouldMatchOfferBooking() {
        return minOnePaxShouldMatchOfferBooking;
    }

    public void setMinOnePaxShouldMatchOfferBooking(boolean minOnePaxShouldMatchOfferBooking) {
        this.minOnePaxShouldMatchOfferBooking = minOnePaxShouldMatchOfferBooking;
    }
}
