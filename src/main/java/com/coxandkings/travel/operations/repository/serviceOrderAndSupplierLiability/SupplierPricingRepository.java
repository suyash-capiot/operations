package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.SupplierPricingCriteria;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.SupplierPricing;

public interface SupplierPricingRepository {

    public SupplierPricing getSupplierPricing(SupplierPricingCriteria criteria);
    public void remove(String id);

}
