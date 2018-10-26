package com.coxandkings.travel.operations.service.holidayinvoice;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.holidayinvoice.HolidayResource;

public interface HolidayFinanceService {
    String generateHolidayInvoice(HolidayResource holidayResource) throws OperationException;

    Boolean checkHolidayInvoiceStatus(String invoiceNumber) throws OperationException;
}
