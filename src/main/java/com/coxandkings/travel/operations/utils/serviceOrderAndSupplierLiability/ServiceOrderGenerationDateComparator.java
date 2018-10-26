package com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;

import java.util.Comparator;

public class ServiceOrderGenerationDateComparator implements Comparator<ServiceOrderResource> {
    @Override
    public int compare(ServiceOrderResource o1, ServiceOrderResource o2) {
        return o1.getDateOfGeneration().compareTo(o2.getDateOfGeneration());
    }
}
