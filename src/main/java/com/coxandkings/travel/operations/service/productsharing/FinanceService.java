package com.coxandkings.travel.operations.service.productsharing;

import com.coxandkings.travel.ext.model.finance.invoice.InvoiceDto;
import com.coxandkings.travel.operations.exceptions.OperationException;

public interface FinanceService {
    /**
     * @param bookingNo
     * @param orderId
     * @return
     * @throws OperationException
     */
    InvoiceDto getInvoicesByBookingDetails(String bookingNo, String orderId) throws OperationException;

    /**
     * @param request
     * @param invoiceNumber
     * @return
     * @throws OperationException
     */
    InvoiceDto amendInvoiceDetails(InvoiceDto request, String invoiceNumber) throws OperationException;

    /**
     * @param request
     * @param oldBookingReferenceNumber
     * @return
     * @throws OperationException
     */
    InvoiceDto cancelInvoiceDetails(InvoiceDto request, String oldBookingReferenceNumber) throws OperationException;
}
