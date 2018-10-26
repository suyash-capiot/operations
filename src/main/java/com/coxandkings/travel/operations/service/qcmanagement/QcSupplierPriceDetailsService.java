package com.coxandkings.travel.operations.service.qcmanagement;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;

public interface QcSupplierPriceDetailsService {
    public Boolean qcCheckForAirSupplierPriceDetails(OpsBooking opsBooking, OpsProduct opsProduct);
    public Boolean qcCheckForAccoSupplierPriceDetails(OpsBooking opsBooking, OpsProduct opsProduct);
}
