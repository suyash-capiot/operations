package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartPaymentSchedule {

    @JsonProperty("paymentScheduleType")
    private String paymentScheduleType;

    @JsonProperty("dueAmount")
    private BigDecimal dueAmount;

    @JsonProperty("dueDate")
    private String dueDate;

    @JsonProperty("isRefundable")
    private Boolean isRefundable;

    public PartPaymentSchedule() {
    }

    public PartPaymentSchedule(String paymentScheduleType, BigDecimal dueAmount, String dueDate, Boolean isRefundable) {
        this.paymentScheduleType = paymentScheduleType;
        this.dueAmount = dueAmount;
        this.dueDate = dueDate;
        this.isRefundable = isRefundable;
    }

    public String getPaymentScheduleType() {
        return paymentScheduleType;
    }

    public void setPaymentScheduleType(String paymentScheduleType) {
        this.paymentScheduleType = paymentScheduleType;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getRefundable() {
        return isRefundable;
    }

    public void setRefundable(Boolean refundable) {
        isRefundable = refundable;
    }
}
