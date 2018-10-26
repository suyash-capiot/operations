package com.coxandkings.travel.operations.service.supplierbillpassing;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.PaymentAdviceGeneration;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierBillPassingResource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SupplierBillPassingService {
     Map singleBillEntry(SupplierBillPassingResource supplierBillPassingResource) throws OperationException, IOException;
     Map commonBillEntry(SupplierBillPassingResource supplierBillPassingResource) throws OperationException, IOException;
     SupplierBillPassingResource get(String id) throws OperationException;
     List<SupplierBillPassing> getAll();
     Map updateStatus(String billPassingId, String status, String remarks) throws OperationException, IOException;
     Map generateCreditDebit(String provisionalServiceOrderId) throws OperationException;
     List<String> getApprovalStatusList();
     List<String> getStatusList();
     Map generatePaymentAdvice(PaymentAdviceGeneration paymentAdviceGeneration) throws OperationException;
     void paymentAdviceAutoGeneration();
     Set<String> getStopPaymentUntilValues();
     Set<ServiceOrderResource> getFinalServiceOrdersByDiscrepancyId(String id);
     String getUserRole(String token) throws UnsupportedEncodingException, OperationException;

    Map getCommonBillPassingResource(ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException;

    Map getPaymentAdviceResource(ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException;

    SupplierBillPassingResource getSingleEntryResource(String provisionalServiceOrderId) throws OperationException;

     PaymentAdviceGeneration getPaymentAdviceById(String id) throws OperationException;

    Map getAttachedServiceOrders(List<String> attachedServiceOrderIds, Integer page, Integer size, String sortCriteria, boolean descending) throws OperationException;

    Map<String,Object> getPaymentAdviceAttachedServiceOrders(List<String> attachedServiceOrderIds, Integer page, Integer size, String sortCriteria, boolean descending) throws OperationException;

    Map<String, Object> getPaymentAdviceResourceByDiscrepancyId(String id);
}
