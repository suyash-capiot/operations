package com.coxandkings.travel.operations.enums.amendclientcommercials;

public enum BEInboundOperation {
	
	SEARCH("search"),
	REPRICE("reprice"),
	BOOK("book"),
    MODIFY("modify"),
	CANCEL("cancel"),
	AMEND("amend");
	
	
	private String inboundOperation;
	
	BEInboundOperation(String inboundOperation) {
		this.inboundOperation=inboundOperation;
	}

	public String getInboundOperation() {
		return inboundOperation;
	}	
	
}
