package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.FinalSupplierLiability;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

public interface FinalSupplierLiabilityService {

    public FinalSupplierLiability generateFSL(ServiceOrderResource resource) throws OperationException, IOException, JSONException;
    public FinalSupplierLiability updateFSL(ServiceOrderResource resource) throws OperationException;
    public FinalSupplierLiability getFSLById(String uniqueId) throws OperationException;
    public ServiceOrderResource getFSLResourceById(String uniqueId) throws OperationException;
    public FinalSupplierLiability getFSLByVersionId(VersionId versionId) throws OperationException;
    public Map<String, Object> getFinalSupplierLiabilities(ServiceOrderSearchCriteria searchCriteria) throws OperationException, IOException, JSONException;
}
