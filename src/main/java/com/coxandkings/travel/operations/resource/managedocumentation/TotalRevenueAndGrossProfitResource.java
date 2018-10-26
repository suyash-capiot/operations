package com.coxandkings.travel.operations.resource.managedocumentation;

import java.math.BigDecimal;

public class TotalRevenueAndGrossProfitResource {

    private String companyId;
    private String entityType;
    private String entityId;
    private String fromDateRange;
    private String toDateRange;
    private BigDecimal totalRevenueAmount;
    private BigDecimal totalGrossProfitAmount;
    private Integer noOfBookings;
    private String bookId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getFromDateRange() {
        return fromDateRange;
    }

    public void setFromDateRange(String fromDateRange) {
        this.fromDateRange = fromDateRange;
    }

    public String getToDateRange() {
        return toDateRange;
    }

    public void setToDateRange(String toDateRange) {
        this.toDateRange = toDateRange;
    }

    public BigDecimal getTotalRevenueAmount() {
        return totalRevenueAmount;
    }

    public void setTotalRevenueAmount(BigDecimal totalRevenueAmount) {
        this.totalRevenueAmount = totalRevenueAmount;
    }

    public BigDecimal getTotalGrossProfitAmount() {
        return totalGrossProfitAmount;
    }

    public void setTotalGrossProfitAmount(BigDecimal totalGrossProfitAmount) {
        this.totalGrossProfitAmount = totalGrossProfitAmount;
    }

    public Integer getNoOfBookings() {
        return noOfBookings;
    }

    public void setNoOfBookings(Integer noOfBookings) {
        this.noOfBookings = noOfBookings;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
