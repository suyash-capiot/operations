package com.coxandkings.travel.operations.resource.doTicketing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendForApprovalResource {

    private String id;
    private String module;
    private String opsApprovalType;
    private String reasonForRequest;
    private String userId;
    private String userName;
    private String bookId;
    private String orderId;

    private String scenario;
    private BigDecimal companyAmount;
    private BigDecimal customerAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }


    public String getOpsApprovalType() {
        return opsApprovalType;
    }

    public void setOpsApprovalType(String opsApprovalType) {
        this.opsApprovalType = opsApprovalType;
    }

    public String getReasonForRequest() {
        return reasonForRequest;
    }

    public void setReasonForRequest(String reasonForRequest) {
        this.reasonForRequest = reasonForRequest;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

}
