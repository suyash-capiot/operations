package com.coxandkings.travel.operations.repository.supplierbillpassing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierInvoiceOCR;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierInvoiceSearchCriteria;

import java.util.List;

public interface SupplierInvoiceOCRRepo {

    SupplierInvoiceOCR add(SupplierInvoiceOCR supplierInvoiceOCR);
    SupplierInvoiceOCR getById(String invoiceNumber);
    List<SupplierInvoiceOCR> getAvailableInvoice(SupplierInvoiceSearchCriteria supplierInvoiceSearchCriteria) throws OperationException;
    SupplierInvoiceOCR update(SupplierInvoiceOCR supplierInvoiceOCR);

    SupplierInvoiceOCR getByinvoiceNumber(String invoiceNumber, String supplierId) throws OperationException;
}
