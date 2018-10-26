package com.coxandkings.travel.operations.resource.doTicketing;

import java.math.BigDecimal;

public class RetainRefundResource {

    private String scenario;

    private BigDecimal differenceAmount;

    private BigDecimal companyAmount;

    private BigDecimal customerAmount;

    public RetainRefundResource() {
    }

    public RetainRefundResource(String scenario, BigDecimal differenceAmount, BigDecimal companyAmount, BigDecimal customerAmount) {
        this.scenario = scenario;
        this.differenceAmount = differenceAmount;
        this.companyAmount = companyAmount;
        this.customerAmount = customerAmount;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public BigDecimal getCompanyAmount() {
        return companyAmount;
    }

    public void setCompanyAmount(BigDecimal companyAmount) {
        this.companyAmount = companyAmount;
    }

    public BigDecimal getCustomerAmount() {
        return customerAmount;
    }

    public void setCustomerAmount(BigDecimal customerAmount) {
        this.customerAmount = customerAmount;
    }

    public BigDecimal getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(BigDecimal differenceAmount) {
        this.differenceAmount = differenceAmount;
    }
}
