package com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing;

import com.coxandkings.travel.operations.service.commercialstatements.impl.Priority;

public class SlabProperties {

	@Priority(value = 1)
	private String productCateg;
	@Priority(value = 2)
	private String productCategSubType;
	@Priority(value = 4)
	private String supplierID;
	@Priority(value = 3)
	private String market; 
	
	public String getProductCateg() {
		return productCateg;
	}

	public void setProductCateg(String productCateg) {
		this.productCateg = productCateg;
	}

	public String getProductCategSubType() {
		return productCategSubType;
	}

	public void setProductCategSubType(String productCategSubType) {
		this.productCategSubType = productCategSubType;
	}

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}
	
}
