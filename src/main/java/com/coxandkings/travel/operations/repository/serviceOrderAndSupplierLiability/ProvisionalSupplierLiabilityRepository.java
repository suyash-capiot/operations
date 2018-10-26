package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalSupplierLiability;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;

import java.util.Map;

public interface ProvisionalSupplierLiabilityRepository {

    public ProvisionalSupplierLiability generatePSL(ProvisionalSupplierLiability provisionalSupplierLiability);
    public ProvisionalSupplierLiability updatePSL(ProvisionalSupplierLiability provisionalSupplierLiability);
    public ProvisionalSupplierLiability getPSLByVersionId(VersionId versionId);
    public Map<String, Object> getProvisionalSupplierLiabilities(ServiceOrderSearchCriteria searchCriteria);
    public Long getCount();

}
