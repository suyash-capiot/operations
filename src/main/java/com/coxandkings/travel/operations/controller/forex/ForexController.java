package com.coxandkings.travel.operations.controller.forex;

import com.coxandkings.travel.operations.consumer.listners.ForexBookingListener;
import com.coxandkings.travel.operations.consumer.listners.impl.ForexBookingListenerImpl;
import com.coxandkings.travel.operations.consumer.listners.impl.ServiceOrderAndSupplierLiabilityListener;
import com.coxandkings.travel.operations.criteria.forex.ForexCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.resource.forex.ForexBookingResource;
import com.coxandkings.travel.operations.resource.forex.ForexCountResource;
import com.coxandkings.travel.operations.resource.forex.PassengerNameResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.forex.ForexBookingService;
import com.coxandkings.travel.operations.service.forex.ForexIndentService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/forex")
public class ForexController {

    private Logger logger = Logger.getLogger(ForexBookingService.class);

    @Autowired
    private ForexBookingService forexBookingService;

    @Autowired
    private ForexIndentService forexIndentService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @PostMapping("/getByCriteria")
    public HttpEntity<Object> getBookingByCriteria(@RequestBody ForexCriteria forexCriteria,
                                                   @RequestParam(value = "workflow", required = true) boolean workflow) throws OperationException {

        if (workflow == true) {

            Map<String, Object> workFlowRsResults;
            workFlowRsResults = forexBookingService.getWorkflowList(forexCriteria);
            return new ResponseEntity<>(workFlowRsResults, HttpStatus.OK);
        }

        Map<String, Object> masterRSResults = forexBookingService.getByCriteria(forexCriteria);
        return new ResponseEntity<>(masterRSResults, HttpStatus.OK);

    }

    //Temporary
    @PostMapping(value = "/kafkaBookReq", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String populateForexBooking(InputStream kafka) throws OperationException, IOException {


        ObjectMapper mapper = new ObjectMapper();
        KafkaBookingMessage message = mapper.readValue(new JSONObject(new JSONTokener(kafka)).toString(), KafkaBookingMessage.class);
        OpsBooking opsBooking = opsBookingService.getBooking(message.getBookId());

        try {
            ForexBookingListener forexBookingListener = context.getBean(ForexBookingListenerImpl.class);
            forexBookingListener.processBooking(opsBooking, message);
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failure";
    }

    @PutMapping("/request/saveOrUpdate")
    public ForexBooking saveOrUpdate(@RequestBody ForexBookingResource resource) throws OperationException {

        return forexBookingService.saveOrUpdate(resource);
    }

    @GetMapping(value = "/getClientPaymentDetails/{requestId}")
    public ResponseEntity<ForexBooking> getClientPaymentDetails(@PathVariable("requestId") String requestId) throws OperationException {

        ForexBooking res = forexBookingService.getForexByID(requestId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value = "/autosuggestion/clientName" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getClientNames(InputStream stream) throws OperationException {

        JSONObject object = new JSONObject(new JSONTokener(stream));
        ResponseEntity<Object> res = null;
        JSONArray array = clientMasterDataService.getClientNames(object);
        return new ResponseEntity<>(array, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateRecord(@PathVariable("id") String id,
                                           @RequestBody ForexBookingResource resource,
                                           @RequestParam(value = "workflow", required = false) String workflow) throws OperationException{
        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            ForexBooking forexBooking = forexBookingService.updateRecord(id, resource, workflow);
            return new ResponseEntity<>(forexBooking, HttpStatus.OK);

        }  catch (Exception e) {
            logger.error("Exception occurred while updating record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/testIndentSave/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateTestRecord(@PathVariable("id") String id,
                                               @RequestBody ForexBookingResource resource)
                                              throws OperationException{
        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            ForexBooking forexBooking = forexBookingService.updateRecord(id, resource, null);
            return new ResponseEntity<>(forexBooking, HttpStatus.OK);

        }  catch (Exception e) {
            logger.error("Exception occurred while updating record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> masterUpdateView(@PathVariable("id") String requestId,
                                               @RequestParam(value = "workflow") String workflow) throws OperationException{
        if (requestId == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            ForexBooking forexBooking = forexBookingService.updateRecord(requestId, null, workflow);
            return new ResponseEntity<>(forexBooking, HttpStatus.OK);

        }  catch (Exception e) {
            logger.error("Exception occurred while updating record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/request/submit")
    public ForexBooking submitForApproval(@RequestBody ForexBookingResource resource) throws OperationException {
        return forexBookingService.submit(resource);
    }

    @RequestMapping(value = "/releaseLock/{requestId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> releaseLock(@PathVariable("requestId") String requestId) throws OperationException{

        ForexBooking forexBooking = forexBookingService.releaseEditLock(requestId);
        return new ResponseEntity<>(forexBooking, HttpStatus.OK);
    }

    @PostMapping(value = "/request/{requestId}/approve")
    public ResponseEntity<ForexBooking> approveForexIndent(@PathVariable("requestId") String requestId, @RequestBody String remarks) throws OperationException {
        ForexBooking forexBooking = forexBookingService.approveForexRequest(requestId, remarks);
        return new ResponseEntity<>(forexBooking, HttpStatus.OK);
    }

    @PostMapping(value = "/request/{forexId}/reject")
    public ResponseEntity<ForexBooking> rejectForexIndent(@PathVariable("requestId") String requestId, @RequestBody String remarks) throws OperationException {
        ForexBooking forexBooking = forexBookingService.rejectForexRequest(requestId, remarks);
        return new ResponseEntity<>(forexBooking, HttpStatus.OK);
    }

    @GetMapping(value = "/getCountByStatus")
    public ResponseEntity<ForexCountResource> getCountByStatus() throws OperationException {

        ForexCountResource res = forexBookingService.getCountByStatus();
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @GetMapping(value = "/getById/{forexId}")
    public ResponseEntity<ForexBooking> getForexById(@PathVariable("forexId") String id) throws OperationException {

        ForexBooking res = forexBookingService.getForexByID(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/getByRequestId/{requestId}")
    public ResponseEntity<ForexBooking> getForexByRequestId(@PathVariable("requestId") String id) throws OperationException {

        ForexBooking res = forexBookingService.getForexByRequestID(id);
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @GetMapping(value = "/bookReferenceList/{bookRef}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAutoSuggestBookRefList(@PathVariable("bookRef") String bookRef) throws OperationException {

        List<String> res = forexBookingService.getBookRefListForGivenString(bookRef);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/bookReferenceList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getBookRefList() throws OperationException {

        List<String> res = forexBookingService.getBookRefList();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/supplierList/{supplierName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAutoSuggestSupplierList(@PathVariable("supplierName") String name) throws OperationException {

        List<String> res = forexIndentService.getSupplierListForGivenName(name);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/supplierList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getSupplierList() throws OperationException {

        List<String> res = forexIndentService.getSupplierList();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/clientList/{clientName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAutoSuggestClientList(@PathVariable("clientName") String name) throws OperationException {

        List<String> res = forexBookingService.getClientListForGivenName(name);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/clientList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getClientList() throws OperationException {

        List<String> res = forexBookingService.getClientList();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/paxList/{paxName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PassengerNameResource>> getAutoSuggestPaxList(@PathVariable("paxName") String name) throws OperationException {

        List<PassengerNameResource> res = forexBookingService.getPaxListForGivenName(name);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/reqOrIndentIds/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAutoSuggestReqOrIndentIds(@PathVariable("id") String id) throws OperationException {

        List<String> res = forexBookingService.getReqOrIndentIds(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //Temporarily to test
    @PostMapping(value = "/generatePSO", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String generateServiceOrder(InputStream kafka) throws OperationException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        KafkaBookingMessage message = mapper.readValue(new JSONObject(new JSONTokener(kafka)).toString(), KafkaBookingMessage.class);
        OpsBooking opsBooking = opsBookingService.getBooking(message.getBookId());
        try {
            ServiceOrderAndSupplierLiabilityListener serviceOrderListener = context.getBean(ServiceOrderAndSupplierLiabilityListener.class);
            serviceOrderListener.processBooking(opsBooking, message);
            return "success";
        } catch (Exception e) {
            logger.error("Error occured in processing message - ServiceOrderAndSupplierLiabilityListener", e);
        }
        return "failure";
    }



}
