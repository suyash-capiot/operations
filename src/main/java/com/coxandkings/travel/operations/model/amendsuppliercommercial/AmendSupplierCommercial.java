package com.coxandkings.travel.operations.model.amendsuppliercommercial;

import com.coxandkings.travel.operations.enums.amendsuppliercommercial.SupplierCommercialApproval;
import com.coxandkings.travel.operations.model.BaseModel;
import com.coxandkings.travel.operations.model.commercials.ApplyCommercialOn;
import com.coxandkings.travel.operations.model.commercials.BookingIneligibleFor;
import com.coxandkings.travel.operations.model.commercials.SellingPriceComponent;
import com.coxandkings.travel.operations.model.core.OpsProduct;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Table
@Entity
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = JSONUserType.class)})
public class AmendSupplierCommercial extends BaseModel {
    private String bookingId;
    private String createdByUserId;
    private String orderId;
    private String supplierCommercialHead;
    private BigDecimal amount;
    private BigDecimal percentage;
    private Boolean amountApplicable;
    private Boolean addToOriginal;
    private String currency;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "Supplier_Commercial_Selling_PRICE_COMPONENT", joinColumns =
    @JoinColumn(name = "supplier_commercial_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "selling_price_component"))
    private Set<SellingPriceComponent> supplierPriceComponents;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "Supplier_Commercial_APPLY_ON", joinColumns =
    @JoinColumn(name = "supplier_commercial_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "apply_commercial_on"))
    private Set<ApplyCommercialOn> applyProducts;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "Supplier_Commercial_Ineligible_Commercial", joinColumns =
    @JoinColumn(name = "supplier_commercial_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "booking_not_eligible_for"))
    private Set<BookingIneligibleFor> bookingNotEligibleFor;

    /* @OneToMany(mappedBy = "amendSupplierCommercial")
     //@JsonBackReference
     private Set<SupplierPriceComponent> supplierPriceComponents;
     @OneToMany(mappedBy = "amendSupplierCommercial")
     //@JsonBackReference
     private Set<ApplySupplierCommercialOn> applyProducts;
     @OneToMany(mappedBy = "amendSupplierCommercial")
     // @JsonBackReference*/
    // private Set<BookingNotEligibleFor> bookingNotEligibleFor;
    private Boolean sendForApproval;
    private String SupplierName;
    private String roomId;
    private String paxType;
    
    //below are required to create a Todo_Task
    
    private String clientTypeId;
    private String clientId;
    private String companyId;
    
    @Column(name = "is_approved")
    @Enumerated(EnumType.STRING)
    private SupplierCommercialApproval approvalStatus;
    
    @Column(name = "recalculated_commercials")
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "com.coxandkings.travel.operations.model.amendsuppliercommercial.SupplierCommercialResource")})
    private SupplierCommercialResource revisedSupplierCommercialResource;
    @Column(name = "old_commercials")
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "com.coxandkings.travel.operations.model.amendsuppliercommercial.SupplierCommercialResource")})
    private SupplierCommercialResource oldSupplierCommercialResource;

    @Column(name = "updated_ops_order")
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "com.coxandkings.travel.operations.model.core.OpsProduct")})
    private OpsProduct updatedOpsOrder;

    private String todoTaskId;
    
    private String remark;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public String getCreatedByUserId() {
        return createdByUserId;
    }

    @Override
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
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

    public SupplierCommercialApproval getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(SupplierCommercialApproval approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public String getTodoTaskId() {
        return todoTaskId;
    }

    public void setTodoTaskId(String todoTaskId) {
        this.todoTaskId = todoTaskId;
    }

	public OpsProduct getUpdatedOpsOrder() {
		return updatedOpsOrder;
	}

	public void setUpdatedOpsOrder(OpsProduct updatedOpsOrder) {
		this.updatedOpsOrder = updatedOpsOrder;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
