package com.coxandkings.travel.operations.service.holidayinvoice;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.holidayinvoice.HolidayResource;

public interface HolidayInvoiceService {
    String generateInvoice(HolidayResource holidayResource) throws OperationException;
}
