package com.coxandkings.travel.operations.zmock.controller;

import com.coxandkings.travel.operations.zmock.resource.Finance;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping(value = "/finance")
@CrossOrigin(origins = "*")
public class FinanceController {

    ObjectMapper mapper = new ObjectMapper();
    TypeReference<Finance> typeReference = new TypeReference<Finance>() {
    };
    Logger logger = LogManager.getLogger(FinanceController.class);

    @GetMapping(value = "/v1/getSupplierBalanceAmt/{bookingRefId}/{orderId}/{supplierId}", produces = "application/json")
    public Finance getFinanceDetails(@PathVariable("bookingRefId") String bookingRefId,@PathVariable("orderId") String orderId, @PathVariable("supplierId") String supplierId) throws IOException {
        logger.info("method called getFinanceDetails ");
        InputStream inputStream = FinanceController.class.getResourceAsStream("/zmock/json/finance.json");
        Finance finance = mapper.readValue(inputStream, typeReference);

        return finance;
    }


}
