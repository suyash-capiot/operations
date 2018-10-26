package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalSupplierLiability;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

public interface ProvisionalSupplierLiabilityService {

    public ProvisionalSupplierLiability generatePSL(ServiceOrderResource resource) throws OperationException;
    public ProvisionalSupplierLiability updatePSL(ServiceOrderResource resource) throws OperationException;
    public ProvisionalSupplierLiability getPSLById(String uniqueId) throws OperationException;
    public ServiceOrderResource getPSLResourceById(String uniqueId) throws OperationException;
    public ProvisionalSupplierLiability getPSLByVersionId(VersionId versionId) throws OperationException;
    public Map<String,Object> getProvisionalSupplierLiabilities(ServiceOrderSearchCriteria searchCriteria) throws OperationException, IOException, JSONException;

}
