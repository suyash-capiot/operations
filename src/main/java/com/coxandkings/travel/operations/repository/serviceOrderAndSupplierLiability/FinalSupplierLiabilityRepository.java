package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.FinalSupplierLiability;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;

import java.util.Map;

public interface FinalSupplierLiabilityRepository {

    public FinalSupplierLiability generateFSL(FinalSupplierLiability finalSupplierLiability);
    public FinalSupplierLiability updateFSL(FinalSupplierLiability finalSupplierLiability);
    public FinalSupplierLiability getFSLByVersionId(VersionId versionId);
    public Map<String, Object> getFinalSupplierLiabilities(ServiceOrderSearchCriteria searchCriteria);
    public Long getCount();

}
