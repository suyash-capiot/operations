package com.coxandkings.travel.operations.service.holidayinvoice.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.holidayinvoice.HolidayResource;
import com.coxandkings.travel.operations.service.holidayinvoice.HolidayBEService;
import com.coxandkings.travel.operations.service.holidayinvoice.HolidayFinanceService;
import com.coxandkings.travel.operations.service.holidayinvoice.HolidayInvoiceService;
import com.coxandkings.travel.operations.service.refund.RefundFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolidayInvoiceServiceImpl implements HolidayInvoiceService {
    @Autowired
    private HolidayBEService holidayBeService;
    @Autowired
    private HolidayFinanceService holidayFinanceService;
    @Autowired
    private RefundFinanceService refundFinanceService;

    @Override
    public String generateInvoice(HolidayResource holidayResource) throws OperationException {

        String invoiceNumber = refundFinanceService.getInvoicesByBookingDetails(holidayResource.getBookingNo(), holidayResource.getOrderId()).getInvoiceNumber();

        //before update ROE need to check invoice status
        holidayFinanceService.checkHolidayInvoiceStatus(invoiceNumber);
        //updating roe in be
        holidayBeService.updateRoe(holidayResource);
        return holidayFinanceService.generateHolidayInvoice(holidayResource);
    }
}
