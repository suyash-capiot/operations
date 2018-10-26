package com.coxandkings.travel.operations.model.commercialstatements;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementFor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PaymentAdviceStatementInfo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PaymentAdviceStatementInfo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "statementId")
    private String statementId;

    @Column(name = "statementName")
    private String statementName;

    @Column(name = "statementLevelNetPayableToSupplierOrClient")
    private BigDecimal statementLevelNetPayableToSupplierOrClient;

    @Column(name = "statementLevelAmountToBePaidForSupplierOrClient")
    private BigDecimal statementLevelAmountToBePaidForSupplierOrClient;

    @Column(name = "statementLevelBalanceAmtPayableToSupplierOrClient")
    private BigDecimal statementLevelBalanceAmtPayableToSupplierOrClient;

    @Column(name = "commercialStatementFor")
    private String commercialStatementFor;

    public String getCommercialStatementFor() {
        return commercialStatementFor;
    }

    public void setCommercialStatementFor(String commercialStatementFor) {
        this.commercialStatementFor = commercialStatementFor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getStatementName() {
        return statementName;
    }

    public void setStatementName(String statementName) {
        this.statementName = statementName;
    }

    public BigDecimal getStatementLevelNetPayableToSupplierOrClient() {
        return statementLevelNetPayableToSupplierOrClient;
    }

    public void setStatementLevelNetPayableToSupplierOrClient(BigDecimal statementLevelNetPayableToSupplierOrClient) {
        this.statementLevelNetPayableToSupplierOrClient = statementLevelNetPayableToSupplierOrClient;
    }

    public BigDecimal getStatementLevelAmountToBePaidForSupplierOrClient() {
        return statementLevelAmountToBePaidForSupplierOrClient;
    }

    public void setStatementLevelAmountToBePaidForSupplierOrClient(BigDecimal statementLevelAmountToBePaidForSupplierOrClient) {
        this.statementLevelAmountToBePaidForSupplierOrClient = statementLevelAmountToBePaidForSupplierOrClient;
    }

    public BigDecimal getStatementLevelBalanceAmtPayableToSupplierOrClient() {
        return statementLevelBalanceAmtPayableToSupplierOrClient;
    }

    public void setStatementLevelBalanceAmtPayableToSupplierOrClient(BigDecimal statementLevelBalanceAmtPayableToSupplierOrClient) {
        this.statementLevelBalanceAmtPayableToSupplierOrClient = statementLevelBalanceAmtPayableToSupplierOrClient;
    }
}

