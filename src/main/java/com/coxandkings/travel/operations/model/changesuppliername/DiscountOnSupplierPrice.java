package com.coxandkings.travel.operations.model.changesuppliername;

import com.coxandkings.travel.operations.model.BaseModel;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.JSONUserType;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.SupplierCommercialPricingDetailResource;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Discount_on_supplier_price")
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = JSONUserType.class)})
public class DiscountOnSupplierPrice extends BaseModel {
    private Double differenceInAmount;
    private Double retainByCompanyAmount;
    private Double passToClientAmount;
    private String bookingId;
    private String orderId;
    private String roomId;
    private String paxType;
    private String todoId;
    
    private String clientId;
    private String companyId;
    private String clientType;
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "com.coxandkings.travel.operations.model.core.OpsProduct")})
    private OpsProduct newOpsProduct;
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "com.coxandkings.travel.operations.resource.amendsuppliercommercial.SupplierCommercialPricingDetailResource")})
    private SupplierCommercialPricingDetailResource oldPrice;
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "com.coxandkings.travel.operations.resource.amendsuppliercommercial.SupplierCommercialPricingDetailResource")})
    private SupplierCommercialPricingDetailResource revisedPrice;

    public Double getDifferenceInAmount() {
        return differenceInAmount;
    }

    public void setDifferenceInAmount(Double differenceInAmount) {
        this.differenceInAmount = differenceInAmount;
    }

    public Double getRetainByCompanyAmount() {
        return retainByCompanyAmount;
    }

    public void setRetainByCompanyAmount(Double retainByCompanyAmount) {
        this.retainByCompanyAmount = retainByCompanyAmount;
    }

    public Double getPassToClientAmount() {
        return passToClientAmount;
    }

    public void setPassToClientAmount(Double passToClientAmount) {
        this.passToClientAmount = passToClientAmount;
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

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public OpsProduct getNewOpsProduct() {
        return newOpsProduct;
    }

    public void setNewOpsProduct(OpsProduct newOpsProduct) {
        this.newOpsProduct = newOpsProduct;
    }

 
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

	public SupplierCommercialPricingDetailResource getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(SupplierCommercialPricingDetailResource oldPrice) {
		this.oldPrice = oldPrice;
	}

	public SupplierCommercialPricingDetailResource getRevisedPrice() {
		return revisedPrice;
	}

	public void setRevisedPrice(SupplierCommercialPricingDetailResource revisedPrice) {
		this.revisedPrice = revisedPrice;
	}

   
}
