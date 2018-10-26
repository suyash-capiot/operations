package com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class ChequeOrDdDetailsResource {

    private String id;

    private String depositedBankName;

    private String chequeOrDdNumber;

    private String accountNumber;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime chequeOrDdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepositedBankName() {
        return depositedBankName;
    }

    public void setDepositedBankName(String depositedBankName) {
        this.depositedBankName = depositedBankName;
    }

    public String getChequeOrDdNumber() {
        return chequeOrDdNumber;
    }

    public void setChequeOrDdNumber(String chequeOrDdNumber) {
        this.chequeOrDdNumber = chequeOrDdNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public ZonedDateTime getChequeOrDdDate() {
        return chequeOrDdDate;
    }

    public void setChequeOrDdDate(ZonedDateTime chequeOrDdDate) {
        this.chequeOrDdDate = chequeOrDdDate;
    }
}
