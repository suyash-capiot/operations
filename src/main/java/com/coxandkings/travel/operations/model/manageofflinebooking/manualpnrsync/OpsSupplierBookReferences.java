package com.coxandkings.travel.operations.model.manageofflinebooking.manualpnrsync;

public class OpsSupplierBookReferences {
	String supplierRef;
	String bookRefID;
	String pseudoCityCode;

	public void setBookRefID(String bookRefID) {
		this.bookRefID = bookRefID;
	}

	public void setPseudoCityCode(String pseudoCityCode) {
		this.pseudoCityCode = pseudoCityCode;
	}

	public void setSupplierRef(String supplierRef) {
		this.supplierRef = supplierRef;
	}

	public String getBookRefID() {
		return bookRefID;
	}

	public String getPseudoCityCode() {
		return pseudoCityCode;
	}

	public String getSupplierRef() {
		return supplierRef;
	}

}
