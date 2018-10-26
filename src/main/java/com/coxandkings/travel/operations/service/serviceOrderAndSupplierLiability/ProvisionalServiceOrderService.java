package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProvisionalServiceOrderService {

    public ProvisionalServiceOrder generatePSO(ServiceOrderResource resource) throws OperationException, IOException;
    public ProvisionalServiceOrder updatePSO(ServiceOrderResource resource) throws OperationException;
    public ProvisionalServiceOrder updatePSO(ServiceOrderResource resource , Boolean checkForUserCompany) throws OperationException;
    public ProvisionalServiceOrder getPSOById(String uniqueId, Boolean checkForUserCompany) throws OperationException;
    public ProvisionalServiceOrder getPSOById(String uniqueId) throws OperationException;
    public ServiceOrderResource getPSOResourceById(String uniqueId) throws OperationException;
    public ProvisionalServiceOrder getPSOByVersionId(VersionId versionId) throws OperationException;
    public Map<String, Object> getProvisionalServiceOrders(ServiceOrderSearchCriteria searchCriteria) throws OperationException, IOException, JSONException;
    public Map<String, Object> getProvisionalServiceOrders(ServiceOrderSearchCriteria searchCriteria, Boolean checkForUserCompany) throws OperationException, IOException, JSONException;
    public Map<String,String> updateProvisionalServiceOrderStatusToCancelled(ServiceOrderResource resource) throws OperationException;
    List<ProvisionalServiceOrder> supplierBillPassingScheduler();
    void releasePaymentScheduler();
    ProvisionalServiceOrder updateProvisionalServiceOrder(ProvisionalServiceOrder provisionalServiceOrder);

    void generatePSO(String bookId, String orderId) throws OperationException;
}
