package com.coxandkings.travel.operations.enums.amendclientcommercials;

public enum BEOperationId {

	SUPPLIER_INTEGRATION_RQ("SUPPINTG_RQ"),
	SUPPLIER_INTEGRATION_RS("SUPPINTG_RS"),
	CLIENT_COMMERCIAL_RQ("CLNTCOMM_RQ"),
	SUPPLIER_COMMERCIAL_RQ("SUPPCOMM_RQ"),
	SUPPLIER_COMMERCIAL_RS("SUPPCOMM_RS"),
    BOOKING_ENGINE_RQ("BKNGENG_RQ"),
    BOOKING_ENGINE_RS("BKNGENG_RS");
	
	private String operationid;
	
	BEOperationId(String operationid){
		this.operationid=operationid;
	}

	public String getOperationid() {
		return operationid;
	}
	
	
}
