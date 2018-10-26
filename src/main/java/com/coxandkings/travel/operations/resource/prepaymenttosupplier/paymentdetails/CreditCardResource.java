package com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class CreditCardResource {

    private String id;
    private String bankName;
    private String cardNumber;
    private String cardType;
    private String nameOnCard;
    private String ccvNumber;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime expiryOfCard;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCcvNumber() {
        return ccvNumber;
    }

    public void setCcvNumber(String ccvNumber) {
        this.ccvNumber = ccvNumber;
    }

    public ZonedDateTime getExpiryOfCard() {
        return expiryOfCard;
    }

    public void setExpiryOfCard(ZonedDateTime expiryOfCard) {
        this.expiryOfCard = expiryOfCard;
    }
}
