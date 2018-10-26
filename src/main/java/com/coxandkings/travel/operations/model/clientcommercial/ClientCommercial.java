package com.coxandkings.travel.operations.model.clientcommercial;


import com.coxandkings.travel.operations.enums.amendclientcommercials.ApprovalStatus;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.BaseModel;
import com.coxandkings.travel.operations.model.commercials.ApplyCommercialOn;
import com.coxandkings.travel.operations.model.commercials.BookingIneligibleFor;
import com.coxandkings.travel.operations.model.commercials.SellingPriceComponent;
import com.coxandkings.travel.operations.model.companycommercial.JSONUserType;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.amendentitycommercial.CommercialResource;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "CLIENT_COMMERCIAL")
@TypeDefs( {@TypeDef( name= "StringJsonObject", typeClass = JSONUserType.class)})
public class ClientCommercial extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "booking_id")
	private String bookingId;
	@Column(name = "order_id")
    private String orderId;
	@Column(name = "product_subcategory")
	private OpsProductSubCategory productSubCategory;
	@Column(name = "supplier_name")
	private String supplierName;
	@Column(name = "room_id")
	private String roomId;
	@Column(name = "room_category_name")
	private String roomCategoryName;
	@Column(name = "room_type_name")
	private String roomTypeName;
	@Column(name = "paxType")
    private String paxType;
	@Column(name = "commercial_entity_id")
    private String commercialEntityID;
	/*@Column(name  = "todo_task_id", unique=true)
    private String todoTaskId;*/
	@Column(name = "commercial_head")
    private String commercialHead;
	@Column(name = "amount")
    private BigDecimal amount;
	@Column(name = "percentage")
    private BigDecimal percentage;
	@Column(name = "amount_applicable")
    private Boolean amountApplicable;
	@Column(name = "add_to_original")
    private Boolean addToOriginal;
	@Column(name = "currency")
    private String currency;

    @Column(name = "marginIncreased")
    private boolean marginIncreased;
	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name="Client_Commercial_Selling_Price_Component",joinColumns =
            @JoinColumn(name = "client_commercial_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "selling_price_component"))
    private Set<SellingPriceComponent> sellingPriceComponent;
	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name="Client_Commercial_Apply_On",joinColumns =
            @JoinColumn(name = "client_commercial_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "apply_commercial_on"))
    private Set<ApplyCommercialOn> applyOnProducts;
	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name="Client_Commercial_Ineligible_Commercial",joinColumns =
            @JoinColumn(name = "client_commercial_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "booking_not_eligible_for"))
    private Set<BookingIneligibleFor> bookingNotEligibleFor;
    
    @Column(name  = "is_approved")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Column(name="approver_remarks")
    private String approverRemarks;
    
    @Column(name  = "customer_approval_status")
    private Boolean customerApprovalStatus;
    @Column(name  = "client_type")
    private String clientType;
    @Column(name  = "client_id")
    private String clientId;
    @Column(name = "company_id")
    private String companyId;
    
    
    @Column(name  = "original_commercials")
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "com.coxandkings.travel.operations.resource.amendentitycommercial.CommercialResource")})
    private CommercialResource originalCommercials;
    
    public CommercialResource getOriginalCommercials() {
		return originalCommercials;
	}
	public void setOriginalCommercials(CommercialResource originalCommercials) {
		this.originalCommercials = originalCommercials;
	}
	@Column(name  = "recalculated_commercials")
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "com.coxandkings.travel.operations.resource.amendentitycommercial.CommercialResource")})
    private CommercialResource recalculatedCommercials;


    @Column(name = "updated_ops_order")
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "com.coxandkings.travel.operations.model.core.OpsProduct")})
    private OpsProduct updatedOpsOrder;
    
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
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	 public ApprovalStatus getApprovalStatus() {
	    return approvalStatus;
	}
    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
	/*public String getTodoTaskId() {
		return todoTaskId;
	}
	public void setTodoTaskId(String todoTaskId) {
		this.todoTaskId = todoTaskId;
	}*/
	public String getCommercialEntityID() {
		return commercialEntityID;
	}
	public void setCommercialEntityID(String commercialEntityID) {
		this.commercialEntityID = commercialEntityID;
	}
	public CommercialResource getRecalculatedCommercials() {
		return recalculatedCommercials;
	}
	public void setRecalculatedCommercials(CommercialResource recalculatedCommercials) {
		this.recalculatedCommercials = recalculatedCommercials;
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
	public String getApproverRemarks() {
		return approverRemarks;
	}
	public void setApproverRemarks(String approverRemarks) {
		this.approverRemarks = approverRemarks;
	}
	public OpsProductSubCategory getProductSubCategory() {
		return productSubCategory;
	}
	public void setProductSubCategory(OpsProductSubCategory productSubCategory) {
		this.productSubCategory = productSubCategory;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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
