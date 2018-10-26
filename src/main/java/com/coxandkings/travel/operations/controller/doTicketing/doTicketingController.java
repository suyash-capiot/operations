package com.coxandkings.travel.operations.controller.doTicketing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.doTicketing.ApproveRejectResource;
import com.coxandkings.travel.operations.resource.doTicketing.DoTicketingResource;
import com.coxandkings.travel.operations.resource.doTicketing.SendForApprovalResource;
import com.coxandkings.travel.operations.service.doTicketing.DoTicketingService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doTicketing")
@CrossOrigin(value = "*")
public class doTicketingController {

    @Autowired
    private DoTicketingService doTicketingService;

    @GetMapping(value = "/v1/reprice")
    public ResponseEntity<Object> reprice(@RequestParam(value = "bookId") String bookId,
                                          @RequestParam(value = "orderId") String orderId) throws OperationException {
        //Return the Old Amount,New Amount and difference amount.
        Object repriceAmountResource = null;
        try {
            repriceAmountResource = doTicketingService.repriceForAir(bookId, orderId);
            return new ResponseEntity<>(repriceAmountResource, HttpStatus.OK);
        }
        catch(OperationException e) {
            throw e;
        }
        catch(Exception x){
            throw new OperationException("Error occurred in performing Re-price");
        }
    }

    @GetMapping(value = "/v1/book")
    public ResponseEntity<JSONObject> book(@RequestParam(value = "bookId") String bookId,
                                           @RequestParam(value = "orderId") String orderId) throws OperationException {
        //Return the Old Amount,New Amount and difference amount.
        JSONObject response;
        try {
            response = doTicketingService.repriceAndBook(bookId, orderId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(OperationException e) {
            throw e;
        }
        catch(Exception x){
            throw new OperationException("Error occurred in performing Book");
        }
    }

    @GetMapping(value = "/v1/issueTicket", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> issueTicket(@RequestParam(value = "bookId") String bookId,
                                              @RequestParam(value = "orderId") String orderId) throws OperationException {

        JSONObject doTicketing;
        try {
            doTicketing = doTicketingService.issueTicketForAir(bookId, orderId);
            return new ResponseEntity<>(doTicketing.toString(), HttpStatus.OK);
        }
        catch(OperationException e) {
            throw e;
        }
        catch(Exception x){
            throw new OperationException("Error occurred in performing Issue Ticket");
        }
    }


    @PostMapping(value = "/v1/issueTicket")
    public ResponseEntity<DoTicketingResource> issueTicket(@RequestBody DoTicketingResource doTicketingResource) throws OperationException {

        DoTicketingResource doTicketing;
        try {
            doTicketing = doTicketingService.repriceAndCalculateDifference(doTicketingResource);
            return new ResponseEntity<>(doTicketing, HttpStatus.OK);
        }
        catch(OperationException e) {
            throw e;
        }
        catch(Exception x){
            throw new OperationException("Error occurred in performing Issue Ticket");
        }
    }

    @GetMapping(value = "/v1/{id}")
    public ResponseEntity<DoTicketingResource> getRecord(@PathVariable(value = "id") String id) throws OperationException{

        try{
           DoTicketingResource doTicketingResource = doTicketingService.getDoTicketing(id);
           return new ResponseEntity<>(doTicketingResource, HttpStatus.OK);
        }catch (Exception x){
            throw new OperationException("Error occurred in fetching record");
        }
    }

    @GetMapping(value = "/v1/{bookId}/{orderId}")
    public ResponseEntity<DoTicketingResource> doTicketing(@PathVariable("bookId") String bookId,
                                   @PathVariable("orderId") String orderId) throws OperationException{
        try{
            DoTicketingResource doTicketingResource = doTicketingService.getDoTicketing(bookId, orderId);
            return new ResponseEntity<>(doTicketingResource, HttpStatus.OK);
        }catch(OperationException e){
            throw e;
        }catch (Exception x){
            throw new OperationException("Error occurred in validating Issue Ticket");
        }
    }

    @GetMapping(value = "/v1/validate")
    public void validateIfIssueTicketCanBePerformed(@RequestParam(value = "bookId") String bookId,
                                                    @RequestParam(value = "orderId") String orderId) throws OperationException{

        try{
            doTicketingService.issueTicketValidator(bookId, orderId);
        }catch(OperationException e){
            throw e;
        }catch (Exception x){
            throw new OperationException("Error occurred in validating Issue Ticket");
        }
    }

    @PostMapping(value = "/v1/sendForApproval")
    public ResponseEntity<DoTicketingResource> sendForApproval(@RequestBody SendForApprovalResource sendForApprovalResource) throws OperationException{

        try{
            DoTicketingResource doTicketingResource = doTicketingService.sendForApproval(sendForApprovalResource);
            return new ResponseEntity<>(doTicketingResource, HttpStatus.OK);
        }catch(OperationException e){
            throw e;
        }catch (Exception x){
            throw new OperationException("Error occurred in validating Issue Ticket");
        }
    }

    @PostMapping(value = "/v1/approveOrReject")
    public ResponseEntity<DoTicketingResource> approve(@RequestBody ApproveRejectResource approveRejectResource) throws OperationException{

        try{
            DoTicketingResource doTicketingResource = doTicketingService.approveOrReject(approveRejectResource);
            return new ResponseEntity<>(doTicketingResource, HttpStatus.OK);
        }catch(OperationException e){
            throw e;
        }catch (Exception x){
            throw new OperationException("Error occurred in approving/rejecting");
        }
    }

    @PostMapping(value = "/v1/retainOrRefund")
    public ResponseEntity<DoTicketingResource> retainOrRefund(@RequestBody DoTicketingResource doTicketingResource) throws OperationException {
        DoTicketingResource response;
        try {
            response = doTicketingService.retainOrRefund(doTicketingResource);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(OperationException e) {
            throw e;
        }
        catch(Exception x){
            throw new OperationException("Error occurred in performing Issue Ticket");
        }
    }

    @GetMapping(value = "/v1/credentials/{bookId}/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSupplierCredentials(@PathVariable("bookId") String bookId,
                                                         @PathVariable("orderId") String orderId) throws OperationException {
        JSONArray response;
        try {
            response = doTicketingService.getSupplierCredentials(bookId, orderId);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        }
        catch(OperationException e) {
            throw e;
        }
        catch(Exception x){
            throw new OperationException("Unable to fetch credentials list");
        }
    }




}
