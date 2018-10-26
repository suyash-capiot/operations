package com.coxandkings.travel.operations.criteria.prepaymenttosupplier;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PaymentCriteria {

    private String id;
    private String supplierReferenceId;
    private String orderId;
    private String bookingRefId;
    private String supplierName;
    private String paymentAdviceNumber;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime paymentDueDateFrom;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime paymentDueDateTo;

    private BigDecimal orderLevelAmountPayableToSupplier;
    private BigDecimal orderLevelNetPayableAmt;
    private BigDecimal balanceAmtToBePaidToSupplier;
    private String serviceOrderId;
    private Boolean prePayment;

    //pagination
    private Integer pageSize;
    private Integer pageNumber;

    private Boolean searchByPaymentAdviceNumber;

    public Boolean getSearchByPaymentAdviceNumber() {
        return searchByPaymentAdviceNumber;
    }

    public void setSearchByPaymentAdviceNumber(Boolean searchByPaymentAdviceNumber) {
        this.searchByPaymentAdviceNumber = searchByPaymentAdviceNumber;
    }

    public String getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(String serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplierReferenceId() {
        return supplierReferenceId;
    }

    public void setSupplierReferenceId(String supplierReferenceId) {
        this.supplierReferenceId = supplierReferenceId;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getPaymentAdviceNumber() {
        return paymentAdviceNumber;
    }

    public void setPaymentAdviceNumber(String paymentAdviceNumber) {
        this.paymentAdviceNumber = paymentAdviceNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public ZonedDateTime getPaymentDueDateFrom() {
        return paymentDueDateFrom;
    }

    public void setPaymentDueDateFrom(ZonedDateTime paymentDueDateFrom) {
        this.paymentDueDateFrom = paymentDueDateFrom;
    }

    public ZonedDateTime getPaymentDueDateTo() {
        return paymentDueDateTo;
    }

    public void setPaymentDueDateTo(ZonedDateTime paymentDueDateTo) {
        this.paymentDueDateTo = paymentDueDateTo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public BigDecimal getOrderLevelAmountPayableToSupplier() {
        return orderLevelAmountPayableToSupplier;
    }

    public void setOrderLevelAmountPayableToSupplier(BigDecimal orderLevelAmountPayableToSupplier) {
        this.orderLevelAmountPayableToSupplier = orderLevelAmountPayableToSupplier;
    }

    public BigDecimal getOrderLevelNetPayableAmt() {
        return orderLevelNetPayableAmt;
    }

    public void setOrderLevelNetPayableAmt(BigDecimal orderLevelNetPayableAmt) {
        this.orderLevelNetPayableAmt = orderLevelNetPayableAmt;
    }

    public BigDecimal getBalanceAmtToBePaidToSupplier() {
        return balanceAmtToBePaidToSupplier;
    }

    public void setBalanceAmtToBePaidToSupplier(BigDecimal balanceAmtToBePaidToSupplier) {
        this.balanceAmtToBePaidToSupplier = balanceAmtToBePaidToSupplier;
    }

    public Boolean isPrePayment() {
        return prePayment;
    }

    public void setPrePayment(Boolean prePayment) {
        this.prePayment = prePayment;
    }
}
