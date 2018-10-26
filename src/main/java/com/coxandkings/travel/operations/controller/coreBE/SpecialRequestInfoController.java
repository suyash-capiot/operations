package com.coxandkings.travel.operations.controller.coreBE;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.service.specialrequest.SpecialRequestService;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/specialRequest")
public class SpecialRequestInfoController {

    private static Logger logger = LogManager.getLogger(SpecialRequestInfoController.class);

    @Autowired
    private SpecialRequestService specialRequestService;

    @GetMapping(value = "/v1/getAllRequestTypes", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<String> getBooking(@RequestParam String bookingId, @RequestParam String orderId) throws OperationException {
        logger.info("-- SpecialRequestController : GET  : /getAllRequestTypes --");
        logger.info("Parameters for above call : " + "(key  : bookingRefId, value : " + bookingId + " )"
                + "(key  : OrderId, value : " + orderId + " )");
        String specialRequests = specialRequestService.getAllRequestTypes(bookingId, orderId);
        logger.info("-- Exit SpecialRequestController : GET  : /getAllRequestTypes --");
        return new ResponseEntity<String>(specialRequests, HttpStatus.OK);
    }

    @PostMapping(value ="/v1/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> updateSrr(InputStream req) throws OperationException {
            JSONTokener jsonTok = new JSONTokener(req);
            JSONObject reqJson = new JSONObject(jsonTok);
            MessageResource messageResource = specialRequestService.updateSSR(reqJson);
            return new ResponseEntity<MessageResource>(messageResource, HttpStatus.OK);
    }


}
