package com.coxandkings.travel.operations.controller.offerDetails;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.offerDetails.OfferDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("offerDetails")
@CrossOrigin(origins = "*")
public class OfferDetailsController {

    private static Logger logger = LogManager.getLogger(OfferDetailsController.class);

    @Autowired
    OfferDetailsService offerDetailsService;

    @GetMapping("/v1/{offerCode}")
    public ResponseEntity<Object> getVoucherCodeDetails(@PathVariable String offerCode)throws OperationException{
        JSONArray voucherDetails = offerDetailsService.getVoucherCodeDetails(offerCode);
        return new ResponseEntity<>(voucherDetails, HttpStatus.OK);
    }
}
