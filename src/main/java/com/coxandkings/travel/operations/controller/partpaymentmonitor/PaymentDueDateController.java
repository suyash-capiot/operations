package com.coxandkings.travel.operations.controller.partpaymentmonitor;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.partPaymentMonitor.PaymentDueDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/test")
public class PaymentDueDateController {

    @Autowired
    private PaymentDueDateService paymentDueDateService;

    @GetMapping("/v1/get/{bookID}")
    public ResponseEntity<ZonedDateTime> get(@PathVariable("bookID") String bookID) throws OperationException {
        return new ResponseEntity<>(paymentDueDateService.calculatePaymentDueDate(bookID), HttpStatus.OK);
    }
}
