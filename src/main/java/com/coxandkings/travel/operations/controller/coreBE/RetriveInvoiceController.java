package com.coxandkings.travel.operations.controller.coreBE;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.booking.InvoiceService;
import com.coxandkings.travel.operations.service.user.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/invoice")
public class RetriveInvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/v1/getLatestInvoice")
    public ResponseEntity<JSONObject> getLatestInvoice(@RequestParam String bookingNo) throws OperationException {
        return new ResponseEntity<>(invoiceService.getLatestInvoice(bookingNo), HttpStatus.OK);
    }
}
