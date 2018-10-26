package com.coxandkings.travel.operations.resource.outbound.be;

public class B2CContactDetails extends  ClientDetails {
    private CorporateAddress corporateAddress;

    public CorporateAddress getCorporateAddress() {
        return corporateAddress;
    }

    public void setCorporateAddress(CorporateAddress corporateAddress) {
        this.corporateAddress = corporateAddress;
    }
}
