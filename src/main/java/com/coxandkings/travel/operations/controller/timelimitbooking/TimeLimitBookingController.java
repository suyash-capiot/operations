package com.coxandkings.travel.operations.controller.timelimitbooking;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.timelimitbooking.TimeLimitExpiryResource;
import com.coxandkings.travel.operations.resource.timelimitbooking.convert.ClientBackgroundInfoCriteriaResource;
import com.coxandkings.travel.operations.resource.timelimitbooking.convert.TLConvertResource;
import com.coxandkings.travel.operations.resource.timelimitbooking.convert.TotalRevenueAndGrossProfitResource;
import com.coxandkings.travel.operations.service.timelimitbooking.MDMTimeLimitService;
import com.coxandkings.travel.operations.service.timelimitbooking.TimeLimitBookingService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/timeLimitBooking")
public class TimeLimitBookingController {
    @Autowired
    TimeLimitBookingService timeLimitBookingService;

    @Autowired
    MDMTimeLimitService mdmTimeLimitService;

    @RequestMapping(value = "/v1/updateTimeLimitExpiry", method = RequestMethod.POST)
    public ResponseEntity updateNewDate(@RequestBody TimeLimitExpiryResource timeLimitExpiryResource) throws ParseException, OperationException {
        return new ResponseEntity<Map>(timeLimitBookingService.updateNewDate(timeLimitExpiryResource), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/convert", method = RequestMethod.POST)
    public ResponseEntity<MessageResource> convertToDefinite(@RequestBody TLConvertResource tlConvertResource) throws OperationException {
        return new ResponseEntity<>(timeLimitBookingService.convertToDefinite(tlConvertResource.getBookID(), tlConvertResource.getOrderID()), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/get/{referenceID}", method = RequestMethod.GET)
    public ResponseEntity<TotalRevenueAndGrossProfitResource> getClientBackgroundInfoByBookID(@PathVariable("referenceID") String referenceID) throws OperationException {
        try {
            return new ResponseEntity<TotalRevenueAndGrossProfitResource>(timeLimitBookingService.getOneYearClientBackgroundInfo(referenceID), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31004);
        }
    }

    @RequestMapping(value = "/v1/getByCriteria", method = RequestMethod.POST)
    public ResponseEntity<TotalRevenueAndGrossProfitResource> getClientBackgroundInfo(@RequestBody ClientBackgroundInfoCriteriaResource criteriaResource) throws OperationException {
        return new ResponseEntity<>(timeLimitBookingService.getClientBackgroundInfo(criteriaResource), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/Approved/{referenceID}", method = RequestMethod.GET)
    public ResponseEntity<MessageResource> approverStatusApproved(@PathVariable("referenceID") String referenceID) throws OperationException {
        try {
            return new ResponseEntity<>(timeLimitBookingService.changeApproverStatusToApproved(referenceID), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31005);
        }
    }

    @RequestMapping(value = "/v1/Rejected/{referenceID}", method = RequestMethod.GET)
    public ResponseEntity<MessageResource> approverStatusRejected(@PathVariable("referenceID") String referenceID) {
        return new ResponseEntity<>(timeLimitBookingService.changeApproverStatusToRejected(referenceID), HttpStatus.OK);
    }
}
