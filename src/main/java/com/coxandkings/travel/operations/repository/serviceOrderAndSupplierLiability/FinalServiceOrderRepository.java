package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.FinalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface FinalServiceOrderRepository {

    public FinalServiceOrder generateFSO(FinalServiceOrder finalServiceOrder);
    public FinalServiceOrder updateFSO(FinalServiceOrder finalServiceOrder);
    public FinalServiceOrder getFSOByVersionId(VersionId versionId);
    public Map<String, Object> getFinalServiceOrders(ServiceOrderSearchCriteria searchCriteria);
    public Map<String, Object> getFinalServiceOrders(ServiceOrderSearchCriteria searchCriteria, Boolean checkForUserCompany);
    public Long getCount();
    void paymentAdviceAutoGeneration();
    public List<String> getAutoSuggestBookId(JSONObject req);
    Map<String,Object> getFSOByIds(List<String> attachedServiceOrderIds, Integer page, Integer size, String sortCriteria, boolean descending);
}
