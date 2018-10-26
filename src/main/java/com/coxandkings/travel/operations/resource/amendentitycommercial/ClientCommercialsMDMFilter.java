package com.coxandkings.travel.operations.resource.amendentitycommercial;

public class ClientCommercialsMDMFilter {

    private String refId;
    private String versionNumber;
    
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public ClientCommercialsMDMFilter(String refId, String versionNumber) {
		super();
		this.refId = refId;
		this.versionNumber = versionNumber;
	}

    


}
