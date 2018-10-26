package com.coxandkings.travel.operations.controller.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.DuplicateBookingsInfoResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/serviceOrderAndSupplierLiabilities")
public class ServiceOrderAndSupplierLiabilityController {

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    @Autowired
    private OpsBookingService opsBookingService;

    @PostMapping(value = "/v1/searchByCriteria")
    public ResponseEntity<Map<String, Object>> getServiceOrdersAndSupplierLiabilities(@RequestBody ServiceOrderSearchCriteria serviceOrderSearchCriteria,
                                                                                      @RequestParam(value = "systemUser", required = false) Boolean isSystemUser) throws OperationException, IOException, JSONException {
        try {
            if(isSystemUser==null)
                isSystemUser=false;
            return new ResponseEntity<>(serviceOrderAndSupplierLiabilityService.getServiceOrdersAndSupplierLiabilities(serviceOrderSearchCriteria, !isSystemUser), HttpStatus.OK);
        } catch(OperationException x){
           throw x;
        } catch(Exception e) {
            throw new OperationException(Constants.OPS_ERR_30622);
        }
    }

    /*@PostMapping(value = "/v1/search")
    public ResponseEntity<Map<String, Object>> getServiceOrdersAndSupplierLiability(@RequestBody SupplierBillPassingSearchCriteria searchCriteria) throws OperationException, IOException, JSONException {
        try {
            Boolean workflow = searchCriteria.getWorkflow();
            Map<String, Object> serviceResults;
            if(workflow==null || !workflow)
                //Get From Workflow (MDM).
                serviceResults = serviceOrderAndSupplierLiabilityService.getServiceOrdersAndSupplierLiabilitiesWorkFlow(searchCriteria);
            else {
                //Get From repository (Master).
                String sortCriteria = searchCriteria.getSort();
                ServiceOrderSearchCriteria serviceOrderSearchCriteria = searchCriteria.getFilter();
                if(sortCriteria!=null && !sortCriteria.isEmpty()){
                    if(searchCriteria.getSort().startsWith("-")) {
                        sortCriteria = searchCriteria.getSort().substring(1);
                        serviceOrderSearchCriteria.setSupplierBillPassingSortingOrder(SupplierBillPassingSortingOrder.ASC);
                    }
                    else
                        serviceOrderSearchCriteria.setSupplierBillPassingSortingOrder(SupplierBillPassingSortingOrder.DESC);

                    serviceOrderSearchCriteria.setSupplierBillPassingSortingCriteria(SupplierBillPassingSortingCriteria.forString(sortCriteria));
                }
                serviceOrderSearchCriteria.setPageNumber(searchCriteria.getPage());
                serviceOrderSearchCriteria.setPageSize(searchCriteria.getCount());
                serviceResults = serviceOrderAndSupplierLiabilityService.getServiceOrdersAndSupplierLiabilities(serviceOrderSearchCriteria);
            }
            return new ResponseEntity<>(serviceResults, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30622);
        }
    }*/

    @PostMapping(value = "/v1/generateSOSLForCancelledProduct")
    public ResponseEntity<Map<String, String>> processSOSLForCancelledProduct(@RequestBody ServiceOrderResource resource) throws OperationException, IOException, JSONException {
        try {
            return new ResponseEntity<>(serviceOrderAndSupplierLiabilityService.processSOSLForCancelledProduct(resource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30623);
        }
    }

    @PostMapping(value = "/v1/linkPaymentAdviceWithServiceOrder")
    public ResponseEntity<String> linkPaymentAdviceWithServiceOrder(@RequestBody PaymentDetails paymentDetails) throws IOException, OperationException {
        return new ResponseEntity<>(serviceOrderAndSupplierLiabilityService.linkPaymentAdviceWithServiceOrder(paymentDetails), HttpStatus.OK);
    }

    @GetMapping(value = "/v1/status")
    public ResponseEntity<List<String>> getServiceOrderStatus() {
        return new ResponseEntity<>(serviceOrderAndSupplierLiabilityService.getServiceOrderStatus(),HttpStatus.OK);
    }

    @GetMapping(value = "/v1/supplierSettlementStatus")
    public ResponseEntity<List<String>> getSupplierSettlementStatus() {
        return new ResponseEntity<>(serviceOrderAndSupplierLiabilityService.getSupplierSettlementStatus(),HttpStatus.OK);
    }

    @GetMapping(value = "/v1/duplicateBookingsInfo/{id}")
    public ResponseEntity<DuplicateBookingsInfoResource> getDuplicateBookingsInfo(@PathVariable("id") String id) throws OperationException {
        return new ResponseEntity<>(serviceOrderAndSupplierLiabilityService.getDuplicateBookingsInfo(id),HttpStatus.OK);
    }

    @PostMapping(value = "/v1/generatePSO")
    public ResponseEntity<String> generateServiceOrder(@RequestBody ServiceOrderResource resource) throws OperationException {
        return new ResponseEntity<>(serviceOrderAndSupplierLiabilityService.generatePSOForDuplicateBookings(resource),HttpStatus.OK);
    }

    @PostMapping(value = "/autosuggest/bookRefNo")
    public ResponseEntity<String> getAutoSuggest(InputStream stream) throws OperationException {

        try {
            JSONTokener tokener = new JSONTokener(stream);
            JSONObject req = new JSONObject(tokener);
            JSONArray response = serviceOrderAndSupplierLiabilityService.getAutoSuggestValues(req);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }

    }

    //For Finance
    @PostMapping(value = "/autosuggest/bookId")
    public ResponseEntity<String> getBookIdAutoSuggest(InputStream stream) throws OperationException {

        try {
            JSONTokener tokener = new JSONTokener(stream);
            JSONObject req = new JSONObject(tokener);
            JSONArray response = serviceOrderAndSupplierLiabilityService.getBookIdAutoSuggest(req);
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }

    }
}
