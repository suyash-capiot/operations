package com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.enums;

public enum OffersType {
	COMPANY_SEARCH_TIME("companySearchTime"),
	COMPANY_REDEEM_TIME("companyRedeemTime"),
	SUPPLIER_SEARCH_TIME("supplierSearchTime"),
	SUPPLIER_REDEEM_TIME("supplierRedeemType");

	private String mTypeString;

	private OffersType(String typeString) {
		mTypeString = typeString;
	}

	public String toString() {
		return mTypeString;
	}
}