package com.coxandkings.travel.operations.resource.amendsuppliercommercial;

import com.coxandkings.travel.operations.model.amendsuppliercommercial.SupplierCommercialResource;
import com.coxandkings.travel.operations.model.commercials.ApplyCommercialOn;
import com.coxandkings.travel.operations.model.commercials.BookingIneligibleFor;
import com.coxandkings.travel.operations.model.commercials.SellingPriceComponent;
import com.coxandkings.travel.operations.model.core.OpsProduct;

import java.math.BigDecimal;
import java.util.Set;

public class AmendSupplierCommercialResource {
    private String bookingId;
    private String createdByUserId;
    private String orderId;
    private String supplierCommercialHead;
    private String mdmRuleId;
    private BigDecimal amount;
    private BigDecimal percentage;
    private Boolean amountApplicable;
    private Boolean addToOriginal;
    private String currency;
    private Set<SellingPriceComponent> supplierPriceComponents;
    private Set<ApplyCommercialOn> applyProducts;
    private Set<BookingIneligibleFor> bookingNotEligibleFor;

    private Boolean sendForApproval;
    private String supplierName;
    private String roomId;
    private String paxType;

    //below are required to create a Todo_Task
    
    private String clientTypeId;
    private String clientId;
    private String companyId;
    private SupplierCommercialResource revisedSupplierCommercialResource;
    private SupplierCommercialResource oldSupplierCommercialResource;
    private OpsProduct updatedOpsOrder;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSupplierCommercialHead() {
        return supplierCommercialHead;
    }

    public void setSupplierCommercialHead(String supplierCommercialHead) {
        this.supplierCommercialHead = supplierCommercialHead;
    }

    public String getMdmRuleId() {
        return mdmRuleId;
    }

    public void setMdmRuleId(String mdmRuleId) {
        this.mdmRuleId = mdmRuleId;
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

    public Set<SellingPriceComponent> getSupplierPriceComponents() {
        return supplierPriceComponents;
    }

    public void setSupplierPriceComponents(Set<SellingPriceComponent> supplierPriceComponents) {
        this.supplierPriceComponents = supplierPriceComponents;
    }

    public Set<ApplyCommercialOn> getApplyProducts() {
        return applyProducts;
    }

    public void setApplyProducts(Set<ApplyCommercialOn> applyProducts) {
        this.applyProducts = applyProducts;
    }

    public Set<BookingIneligibleFor> getBookingNotEligibleFor() {
        return bookingNotEligibleFor;
    }

    public void setBookingNotEligibleFor(Set<BookingIneligibleFor> bookingNotEligibleFor) {
        this.bookingNotEligibleFor = bookingNotEligibleFor;
    }

    public Boolean getSendForApproval() {
        return sendForApproval;
    }

    public void setSendForApproval(Boolean sendForApproval) {
        this.sendForApproval = sendForApproval;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public String getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(String clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public SupplierCommercialResource getRevisedSupplierCommercialResource() {
        return revisedSupplierCommercialResource;
    }

    public void setRevisedSupplierCommercialResource(SupplierCommercialResource revisedSupplierCommercialResource) {
        this.revisedSupplierCommercialResource = revisedSupplierCommercialResource;
    }

    public SupplierCommercialResource getOldSupplierCommercialResource() {
        return oldSupplierCommercialResource;
    }

    public void setOldSupplierCommercialResource(SupplierCommercialResource oldSupplierCommercialResource) {
        this.oldSupplierCommercialResource = oldSupplierCommercialResource;
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
}
