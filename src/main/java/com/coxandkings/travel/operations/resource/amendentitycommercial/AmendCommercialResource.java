package com.coxandkings.travel.operations.resource.amendentitycommercial;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.commercials.ApplyCommercialOn;
import com.coxandkings.travel.operations.model.commercials.BookingIneligibleFor;
import com.coxandkings.travel.operations.model.commercials.SellingPriceComponent;
import com.coxandkings.travel.operations.model.core.OpsProduct;

import java.math.BigDecimal;
import java.util.Set;


public class AmendCommercialResource {

    private String bookingId;
    private String orderId;
    private OpsProductSubCategory productSubCategory;
    private String supplierName;
    private String roomCategoryName;
    private String roomTypeName;
    private String clientMarket;
    private String roomId;
    private String paxType;
    private String createdByUserId;
    private String commercialEntityID;
    private String commercialHead;
    private String mdmRuleId;
    private BigDecimal amount;
    private BigDecimal percentage;
    private Boolean amountApplicable;
    private Boolean addToOriginal;
    private String currency;
    private Set<SellingPriceComponent> sellingPriceComponent;
    private Set<ApplyCommercialOn> applyOnProducts;
    private Set<BookingIneligibleFor> bookingNotEligibleFor;
    private Boolean customerApprovalStatus;
    private CommercialResource originalCommercials;
    private CommercialResource recalculatedCommercials;
    private OpsProduct updatedOpsOrder;
    //below are required to create a Todo_Task
    private String clientType;
    private String clientId;
    private String companyId;
    private boolean marginIncreased;

    public String getMdmRuleId() {
        return mdmRuleId;
    }

    public void setMdmRuleId(String mdmRuleId) {
        this.mdmRuleId = mdmRuleId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCommercialHead() {
        return commercialHead;
    }

    public void setCommercialHead(String commercialHead) {
        this.commercialHead = commercialHead;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Boolean getAmountApplicable() {
        return amountApplicable;
    }

    public void setAmountApplicable(Boolean amountApplicable) {
        this.amountApplicable = amountApplicable;
    }

    public Boolean getAddToOriginal() {
        return addToOriginal;
    }

    public void setAddToOriginal(Boolean addToOriginal) {
        this.addToOriginal = addToOriginal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Set<SellingPriceComponent> getSellingPriceComponent() {
        return sellingPriceComponent;
    }

    public void setSellingPriceComponent(Set<SellingPriceComponent> sellingPriceComponent) {
        this.sellingPriceComponent = sellingPriceComponent;
    }

    public Set<BookingIneligibleFor> getBookingNotEligibleFor() {
        return bookingNotEligibleFor;
    }

    public void setBookingNotEligibleFor(Set<BookingIneligibleFor> bookingNotEligibleFor) {
        this.bookingNotEligibleFor = bookingNotEligibleFor;
    }

    public Boolean getCustomerApprovalStatus() {
        return customerApprovalStatus;
    }

    public void setCustomerApprovalStatus(Boolean customerApprovalStatus) {
        this.customerApprovalStatus = customerApprovalStatus;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public CommercialResource getRecalculatedCommercials() {
        return recalculatedCommercials;
    }

    public void setRecalculatedCommercials(CommercialResource recalculatedCommercials) {
        this.recalculatedCommercials = recalculatedCommercials;
    }

    public String getCommercialEntityID() {
        return commercialEntityID;
    }

    public void setCommercialEntityID(String commercialEntityID) {
        this.commercialEntityID = commercialEntityID;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPaxType() {
        return paxType;
    }

    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    public Set<ApplyCommercialOn> getApplyOnProducts() {
        return applyOnProducts;
    }

    public void setApplyOnProducts(Set<ApplyCommercialOn> applyOnProducts) {
        this.applyOnProducts = applyOnProducts;
    }

    public CommercialResource getOriginalCommercials() {
        return originalCommercials;
    }

    public void setOriginalCommercials(CommercialResource originalCommercials) {
        this.originalCommercials = originalCommercials;
    }

    public String getClientMarket() {
        return clientMarket;
    }

    public void setClientMarket(String clientMarket) {
        this.clientMarket = clientMarket;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public OpsProductSubCategory getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(OpsProductSubCategory productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getRoomCategoryName() {
        return roomCategoryName;
    }

    public void setRoomCategoryName(String roomCategoryName) {
        this.roomCategoryName = roomCategoryName;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public OpsProduct getUpdatedOpsOrder() {
        return updatedOpsOrder;
    }

    public void setUpdatedOpsOrder(OpsProduct updatedOpsOrder) {
        this.updatedOpsOrder = updatedOpsOrder;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public boolean isMarginIncreased() {
        return marginIncreased;
    }

    public void setMarginIncreased(boolean marginIncreased) {
        this.marginIncreased = marginIncreased;
    }
}