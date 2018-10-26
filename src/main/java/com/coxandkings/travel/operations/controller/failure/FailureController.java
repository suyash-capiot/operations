package com.coxandkings.travel.operations.controller.failure;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.booking.AssignStaffResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.failure.*;
import com.coxandkings.travel.operations.service.failure.FailureSchedulerService;
import com.coxandkings.travel.operations.service.failure.FailureService;
import com.coxandkings.travel.operations.utils.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 100000)
@RequestMapping(value = "/failures")
public class FailureController {

    @Autowired
    private FailureService failureService;

    @Autowired
    private FailureSchedulerService failureSchedulerService;

    @PostMapping("/v1/getFailures")
    public List<FailureDetailsResource> getFailures(@RequestBody FailureSearchCriteria failureSearchCriteria) throws OperationException {
        List<FailureDetailsResource> failureDetailsResources = null;
        failureDetailsResources = failureService.getFailures(failureSearchCriteria);
        return failureDetailsResources;

    }

    @PostMapping("/v1/getDuplicates")
    public List<FailureDetailsResource> getDuplicates(@RequestBody DuplicateBookingsResource duplicateBookingsResource) throws OperationException {

        List<FailureDetailsResource> failureDetailsResources = null;
        try {
            failureDetailsResources = failureService.getDuplicates(duplicateBookingsResource);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("Checking for Duplicates not successful");
        }
        return failureDetailsResources;

    }

    @GetMapping("/v1/evokeWebService")
    public ResponseEntity<Map> evokeWebService(@RequestParam String bookID, @RequestParam String orderID) throws OperationException {
        Map<String, String> map = new HashMap<>();
        try {
            return new ResponseEntity(failureService.searchToBook(bookID, orderID), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "Unable to do search and book");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/v1/reportToTechnical")
    public ResponseEntity<String> reportToTechnical(@RequestParam String bookID) throws OperationException {
        String message = null;
        try {
            message = failureService.reportToTechnical(bookID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.TODO_TASK_CREATION_FAIL);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/v1/sendCommunication")
    public ResponseEntity<EmailResponse> sendCommunication(@RequestParam String bookID) throws OperationException {
        EmailResponse emailResponse = failureService.sendCommunication(bookID);
        return new ResponseEntity<>(emailResponse, HttpStatus.OK);
    }

    @GetMapping("/v1/cancel")
    public ResponseEntity cancelBooking(@RequestParam String bookID) {
        Map<String, String> map = new HashMap();
        try {
            return new ResponseEntity(failureService.cancel(bookID), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "Unable to cancel from the Booking Engine");
            return new ResponseEntity(map, HttpStatus.OK);
            //throw new OperationException("Cancellation of the booking is not successful");
        }
    }

    @GetMapping("/v1/getCommunicationCount")
    public ResponseEntity<Integer> getCount(@RequestParam String bookID) throws OperationException {
        Integer count = null;
        try {
            count = failureService.getCommunicationCount(bookID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("Unable to fetch communication count for book ID" + bookID);
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PutMapping("/v1/updatePnr")
    public ResponseEntity<Map> updatePnr(@RequestBody PnrResource pnrResource) throws OperationException {
        try {
            return new ResponseEntity<>(failureService.updatePnr(pnrResource), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("updatePNR is not Successful");
        }
    }

    @PutMapping("/v1/updateSupplierRefNumber")
    public ResponseEntity<Map> updateSupplierRefNumber(@RequestBody SupplierRefResource supplierRefResource) throws OperationException {
        try {
            return new ResponseEntity<>(failureService.updateSupplierRefNumber(supplierRefResource), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("update Supplier reference number is not successful");
        }
    }

    @PutMapping("/v1/updatePaymentDetails")
    public ResponseEntity<Map<String, String>> updatePaymentDetails(@RequestBody PaymentInfoResource paymentInfoResource) throws OperationException {
        try {
            return new ResponseEntity<>(failureService.updatePaymentDetails(paymentInfoResource), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("update Payment details is not successful");
        }
    }

    @GetMapping("/v1/{bookingId}")
    public ResponseEntity<String> startCancellationProcess(@PathVariable("bookingId") String bookingId) throws OperationException {
        return new ResponseEntity<>(failureService.startCancellationProcess(bookingId),HttpStatus.OK);
    }

    @PostMapping("/v1/assignTo")
    public ResponseEntity<JSONObject> assignTo(@RequestBody AssignStaffResource assignStaffResource)throws OperationException{
        JSONObject res = new JSONObject();
        String message = failureService.assignTo(assignStaffResource);
        res.put("message",message);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}
