package com.coxandkings.travel.operations.controller.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalServiceOrderService;
import com.coxandkings.travel.operations.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/provisionalServiceOrders")
public class ProvisionalServiceOrderController {

    @Autowired
    private ProvisionalServiceOrderService provisionalServiceOrderService;

    @PostMapping(value = "/v1/generate")
    public ResponseEntity<ProvisionalServiceOrder> generatePSO(@RequestBody ServiceOrderResource resource) throws OperationException, IOException {
        try {
            return new ResponseEntity<>(provisionalServiceOrderService.generatePSO(resource), HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30611);
        }
    }

    @GetMapping(value = "/v1/generate/bookId/{bookId}/orderId/{orderId}")
    public ResponseEntity<JSONObject> generatePSOManually(@PathVariable(name = "bookId") String bookId,
                                                          @PathVariable(name = "orderId") String orderId) throws OperationException {
        try {
            provisionalServiceOrderService.generatePSO(bookId, orderId);
            return new ResponseEntity<>(new JSONObject().put("message", "PSO Generated Successfully"), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30611);
        }
    }

    //For Finance system user, No check of company from user token, as they might not necessarily match.
    @PostMapping(value = "/v1/update")
    public ResponseEntity<ProvisionalServiceOrder> updatePSO(@RequestBody ServiceOrderResource resource,
                                                             @RequestParam(value = "systemUser", required = false) Boolean isSystemUser) throws OperationException {
        try {
            if (isSystemUser == null)
                isSystemUser = false;
            return new ResponseEntity<>(provisionalServiceOrderService.updatePSO(resource, !isSystemUser), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30612);

        }
    }

    @GetMapping(value = "/v1/{id}")
    public ResponseEntity<ProvisionalServiceOrder> getPSOById(@PathVariable("id") String uniqueId) throws OperationException {
        try {
            return new ResponseEntity<>(provisionalServiceOrderService.getPSOById(uniqueId), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30613);

        }
    }

    @GetMapping(value = "/v1/getPSO")
    public ResponseEntity<ServiceOrderResource> getPSOResourceById(@RequestParam("id") String uniqueId) throws OperationException {
        try {
            return new ResponseEntity<>(provisionalServiceOrderService.getPSOResourceById(uniqueId), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30613);

        }
    }

    @PostMapping(value = "/v1/searchByCriteria")
    public ResponseEntity<Map<String, Object>> getProvisionalServiceOrders(@RequestBody ServiceOrderSearchCriteria criteria,
                                                                           @RequestParam(value = "systemUser", required = false) Boolean isSystemUser) throws OperationException, IOException, JSONException {
        try {
            if (isSystemUser == null)
                isSystemUser = false;
            return new ResponseEntity<>(provisionalServiceOrderService.getProvisionalServiceOrders(criteria, !isSystemUser), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30614);
        }
    }

    @PostMapping(value = "/v1/searchByIdAndVersion")
    public ResponseEntity<ProvisionalServiceOrder> getProvisionalServiceOrder(@RequestBody VersionId resource) throws OperationException {
        try {
            return new ResponseEntity<>(provisionalServiceOrderService.getPSOByVersionId(resource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30615);

        }
    }

    @PostMapping(value = "/v1/updateStatus")
    public ResponseEntity updateProvisionalServiceOrderStatusToCancelled(@RequestBody ServiceOrderResource resource) throws OperationException {
        try {
            return new ResponseEntity(provisionalServiceOrderService.updateProvisionalServiceOrderStatusToCancelled(resource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30616);
        }
    }

}
