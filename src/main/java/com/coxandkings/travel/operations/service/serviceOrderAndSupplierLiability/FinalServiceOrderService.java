package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.FinalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

public interface FinalServiceOrderService {

    public FinalServiceOrder generateFSO(ServiceOrderResource resource) throws OperationException, IOException, JSONException;
    public FinalServiceOrder updateFSO(ServiceOrderResource resource) throws OperationException;
    public FinalServiceOrder getFSOById(String uniqueId) throws OperationException;
    public ServiceOrderResource getFSOResourceById(String uniqueId) throws OperationException;
    public FinalServiceOrder getFSOByVersionId(VersionId versionId) throws OperationException;
    public Map<String, Object> getFinalServiceOrders(ServiceOrderSearchCriteria searchCriteria) throws OperationException, IOException, JSONException;
    public FinalServiceOrder getFSO(ServiceOrderSearchCriteria criteria) throws OperationException;
    public Map<String, Object> getFinalServiceOrders(ServiceOrderSearchCriteria criteria, Boolean checkForUserCompany) throws OperationException, IOException, JSONException;
}
