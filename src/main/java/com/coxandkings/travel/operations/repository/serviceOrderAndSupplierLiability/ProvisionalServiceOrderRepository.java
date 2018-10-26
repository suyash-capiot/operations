package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface ProvisionalServiceOrderRepository {

    public ProvisionalServiceOrder generatePSO(ProvisionalServiceOrder provisionalServiceOrder);
    public ProvisionalServiceOrder updatePSO(ProvisionalServiceOrder provisionalServiceOrder);
    public ProvisionalServiceOrder getPSOByVersionId(VersionId versionId);
    public Map<String, Object> getProvisionalServiceOrders(ServiceOrderSearchCriteria searchCriteria);
    public Map<String, Object> getProvisionalServiceOrders(ServiceOrderSearchCriteria searchCriteria, Boolean checkForUserCompany);
    public Long getCount();
    List<ProvisionalServiceOrder> supplierBillPassingScheduler();
    void releasePaymentsScheduler();

    JSONArray getAutoSuggestBookId(String bookId);

    Map<String, Object> getPSOByIds(List<String> attachedServiceOrderIds, Integer page, Integer size, String sortCriteria, boolean descending);

    List<String> getAutoSuggestBookId(JSONObject req);
}
