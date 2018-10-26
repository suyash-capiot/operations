package com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.BaseServiceOrderDetails;

import java.util.Comparator;

public class ServiceOrderVersionComparator implements Comparator<BaseServiceOrderDetails> {
    @Override
    public int compare(BaseServiceOrderDetails o1, BaseServiceOrderDetails o2) {
        return o2.getVersionNumber().compareTo(o1.getVersionNumber());
    }
}
