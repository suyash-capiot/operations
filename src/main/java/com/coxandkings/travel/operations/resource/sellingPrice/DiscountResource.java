package com.coxandkings.travel.operations.resource.sellingPrice;

import com.coxandkings.travel.operations.resource.BaseResource;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiscountResource extends BaseResource {
    private String bookingRefId;
    private String orderId;

    private String clientCurrencyCode;
    private boolean isHolidayPackage;
    private Boolean isAmount;

    private BigDecimal discountAmount;
    private Double discountPercentage;

    private List<String> sellingPriceComponent;

    public DiscountResource() {
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientCurrencyCode() {
        return clientCurrencyCode;
    }

    public void setClientCurrencyCode(String clientCurrencyCode) {
        this.clientCurrencyCode = clientCurrencyCode;
    }

    public boolean isHolidayPackage() {
        return isHolidayPackage;
    }

    public void setHolidayPackage(boolean holidayPackage) {
        isHolidayPackage = holidayPackage;
    }

    public Boolean getAmount() {
        return isAmount;
    }

    public void setAmount(Boolean amount) {
        isAmount = amount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public List<String> getSellingPriceComponent() {
        return sellingPriceComponent;
    }

    public void setSellingPriceComponent(List<String> sellingPriceComponent) {
        this.sellingPriceComponent = sellingPriceComponent;
    }
}
