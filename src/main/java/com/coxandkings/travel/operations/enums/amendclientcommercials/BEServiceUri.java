package com.coxandkings.travel.operations.enums.amendclientcommercials;

public enum BEServiceUri {

	HOTEL("AccoService"),
	FLIGHT("AirService");
	
	private String serviceUri;
	
	BEServiceUri(String serviceUri){
		this.serviceUri=serviceUri;
	}

	public String getServiceUri() {
		return serviceUri;
	}
	
	
}
