package com.coxandkings.travel.operations.zmock.controller;

import com.coxandkings.travel.operations.model.amendsuppliercommercial.AmendSupplierCommercial;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.SupplierCommercialResource;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

@RequestMapping("/beCommercial")
@RestController
@CrossOrigin(origins = "*")
public class AmendSupplierCommercialBEController {
    private static Logger log = LogManager.getLogger(AmendSupplierCommercialBEController.class);
    @Autowired
    OpsBookingService opsBookingService;

    @PostMapping("/v1/accoRevisedCommercial")
    public HttpEntity<SupplierCommercialResource> getRevisedSupplierCommercialsForAcco(@RequestBody AmendSupplierCommercialResource amendSupplierCommercialResource) {
        ObjectMapper objectMapper = new ObjectMapper();
        SupplierCommercialResource revisedSupplierCommercialResource = null;
        TypeReference<SupplierCommercialResource> typeReference = new TypeReference<SupplierCommercialResource>() {
        };
        InputStream resourceAsStream = AmendSupplierCommercialBEController.class.getClassLoader().getResourceAsStream("zmock/json/be_acco_commercial.json");
        try {
            revisedSupplierCommercialResource = objectMapper.readValue(resourceAsStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Faild to load json");
        }
        return new ResponseEntity<>(revisedSupplierCommercialResource, HttpStatus.OK);
    }

    @PostMapping("/v1/airRevisedCommercial")
    public HttpEntity<SupplierCommercialResource> getRevisedSupplierCommercialsForAir(@RequestBody AmendSupplierCommercialResource amendSupplierCommercialResource) {
        ObjectMapper objectMapper = new ObjectMapper();
        SupplierCommercialResource revisedSupplierCommercialResource = null;
        TypeReference<SupplierCommercialResource> typeReference = new TypeReference<SupplierCommercialResource>() {
        };
        InputStream resourceAsStream = AmendSupplierCommercialBEController.class.getClassLoader().getResourceAsStream("zmock/json/be_air_commercial.json");
        try {
            revisedSupplierCommercialResource = objectMapper.readValue(resourceAsStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Faild to load json");
        }
        return new ResponseEntity<>(revisedSupplierCommercialResource, HttpStatus.OK);
    }

    @PostMapping("/v1/updateBooking")
    public HttpEntity<String> updateBookingForCommercial(@RequestBody AmendSupplierCommercial amendSupplierCommercial) throws ParseException {
        log.info("BooKing Updated");
        return new ResponseEntity<>( "Updated", HttpStatus.OK);
    }
}
