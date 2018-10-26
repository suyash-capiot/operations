package com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails;


import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "CreditCard")
public class CreditCard {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "bankName")
    private String bankName;

    @Column(name = "cardNumber")
    private String cardNumber;

    @Column(name = "cardType")
    private String cardType;

    @Column(name = "nameOnCard")
    private String nameOnCard;

//    @Column(name = "ccvNumber")
//    private String ccvNumber;

    @Column(name = "expiryOfCard")
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

    public ZonedDateTime getExpiryOfCard() {
        return expiryOfCard;
    }

    public void setExpiryOfCard(ZonedDateTime expiryOfCard) {
        this.expiryOfCard = expiryOfCard;
    }
}
