package com.coxandkings.travel.operations.zmock.controller;

import com.coxandkings.travel.operations.service.partPaymentMonitor.PartPaymentMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/partPayments")
@CrossOrigin("*")
public class PartPaymentMonitorController {

    @Autowired
    private PartPaymentMonitorService partPaymentsService;

    @GetMapping(value = "/v1/bookings/getPartPaymentBookings")
    public ResponseEntity getPartPaymentBookings() throws IOException {
        try {
            partPaymentsService.findPartPaymentBookings();
        }
        catch( Exception e )    {
            e.printStackTrace();
        }
        return new ResponseEntity (HttpStatus.OK);
    }
}
