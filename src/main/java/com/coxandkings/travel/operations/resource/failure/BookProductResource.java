package com.coxandkings.travel.operations.resource.failure;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(content = JsonInclude.Include.NON_NULL)
public class BookProductResource {

    private OpsBooking opsBooking;
    private OpsProduct opsProduct;

    public OpsBooking getOpsBooking() {
        return opsBooking;
    }

    public void setOpsBooking(OpsBooking opsBooking) {
        this.opsBooking = opsBooking;
    }

    public OpsProduct getOpsProduct() {
        return opsProduct;
    }

    public void setOpsProduct(OpsProduct opsProduct) {
        this.opsProduct = opsProduct;
    }

}
