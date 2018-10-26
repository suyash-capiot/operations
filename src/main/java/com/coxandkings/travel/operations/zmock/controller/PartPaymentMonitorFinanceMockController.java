package com.coxandkings.travel.operations.zmock.controller;

import com.coxandkings.travel.operations.resource.partpaymentmonitor.PartPaymentBookingsResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/finance")
public class PartPaymentMonitorFinanceMockController {

    ObjectMapper mapper = new ObjectMapper();
    TypeReference<PartPaymentBookingsResource> typeReference = new TypeReference<PartPaymentBookingsResource>(){};

    @GetMapping(value = "/bookings/getPartPaymentBookings")
    public PartPaymentBookingsResource getPartPaymentBookings() throws IOException {
        InputStream inputStream = PartPaymentMonitorFinanceMockController.class.getResourceAsStream("/zmock/json/partPaymentMonitor-getPartPaymentBookings.json" );
        PartPaymentBookingsResource bookingInfo = mapper.readValue(inputStream, typeReference);
        return  bookingInfo;
    }
}
