package com.coxandkings.travel.operations.model.sellingPrice;

import com.coxandkings.travel.operations.enums.sellingPrice.ApprovalStatus;
import com.coxandkings.travel.operations.enums.sellingPrice.DiscountType;
import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table
public class Discount extends BaseModel {
    private String bookingRefId;
    private String orderId;

    private String clientCurrencyCode;
    private boolean isHolidayPackage;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private BigDecimal discountAmount;
    private Double discountPercentage;

    @ElementCollection
    private List<String> sellingPriceComponent;

    @ElementCollection
    private List<String> applyOn;

    public List<String> getApplyOn() {
        return applyOn;
    }

    public void setApplyOn(List<String> applyOn) {
        this.applyOn = applyOn;
    }

    private ZonedDateTime approvalTime;
    private String approver;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    private String approvalTaskId;

    @ElementCollection
    private Map<String, String> additionalInformation;

    private Boolean clientApproved;

    public Discount() {
    }

    public Boolean getClientApproved() {
        return clientApproved;
    }

    public void setClientApproved(Boolean clientApproved) {
        this.clientApproved = clientApproved;
    }

    public String getApprovalTaskId() {
        return approvalTaskId;
    }

    public void setApprovalTaskId(String approvalTaskId) {
        this.approvalTaskId = approvalTaskId;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
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

    public Map<String, String> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, String> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public List<String> getSellingPriceComponent() {
        return sellingPriceComponent;
    }

    public void setSellingPriceComponent(List<String> sellingPriceComponent) {
        this.sellingPriceComponent = sellingPriceComponent;
    }

    public ZonedDateTime getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(ZonedDateTime approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }
}
