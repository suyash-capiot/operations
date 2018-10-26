package com.coxandkings.travel.operations.model.forex.paymentDetails;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ForexChequeOrDdDetails")
public class ForexChequeOrDdDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "depositedBankName")
    private String depositedBankName;

    @Column(name = "chequeOrDdNumber")
    private String chequeOrDdNumber;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "chequeOrDdDate")
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
