package com.coxandkings.travel.operations.controller.holidayinvoicecr;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.holidayinvoice.HolidayResource;
import com.coxandkings.travel.operations.service.holidayinvoice.HolidayInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/holidayInvoice")
public class HolidayInvoiceController {
    @Autowired
    private HolidayInvoiceService holidayInvoiceService;

    @PutMapping("/v1/generateInvoice")
    public String generateInvoice(@RequestBody HolidayResource holidayResource) throws OperationException {
        return holidayInvoiceService.generateInvoice(holidayResource);

    }
}
