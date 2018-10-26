package com.coxandkings.travel.operations.service.supplierbillpassing;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierInvoiceOCR;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierInvoiceId;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierInvoiceSearchCriteria;

import java.util.Map;
import java.util.Set;

public interface SupplierInvoiceOCRService {
    void save(SupplierInvoiceOCR supplierInvoiceOCR) throws OperationException;
//    SupplierBillPassingResource get(String invoiceId) throws OperationException;
    Set<SupplierInvoiceId> getAvailableInvoice(SupplierInvoiceSearchCriteria supplierInvoiceSearchCriteria) throws OperationException;
    SupplierInvoiceOCR update(SupplierInvoiceOCR supplierInvoiceOCR);
    Map getCommercialStatementInvoice(String id, CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;
    Map getSupplierBillPassingResource(String id, ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException;
}
