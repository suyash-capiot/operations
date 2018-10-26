package com.coxandkings.travel.operations.resource.outbound.be;

import java.util.List;

public class B2BClientDetails  extends ClientDetails{

    private List<B2BContactDetails> b2BContactDetails;

    public List<B2BContactDetails> getB2BContactDetails() {
        return b2BContactDetails;
    }

    public void setB2BContactDetails(List<B2BContactDetails> b2BContactDetails) {
        this.b2BContactDetails = b2BContactDetails;
    }
}
