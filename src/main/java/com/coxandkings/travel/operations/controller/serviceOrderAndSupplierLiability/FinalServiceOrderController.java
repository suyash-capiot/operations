package com.coxandkings.travel.operations.controller.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.FinalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.FinalServiceOrderService;
import com.coxandkings.travel.operations.utils.Constants;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/finalServiceOrders")
public class FinalServiceOrderController {

    @Autowired
    private FinalServiceOrderService finalServiceOrderService;

    @PostMapping(value = "/v1/generate")
    public ResponseEntity<FinalServiceOrder> generateFSO(@RequestBody ServiceOrderResource resource) throws OperationException, IOException, JSONException {
        try {
            return new ResponseEntity<>(finalServiceOrderService.generateFSO(resource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30601);
        }
    }

    @PostMapping(value = "/v1/update")
    public ResponseEntity<FinalServiceOrder> updateFSO(@RequestBody ServiceOrderResource resource,
                                                       @RequestParam(value = "systemUser", required = false) Boolean isSystemUser) throws OperationException {
        try {
            return new ResponseEntity<>(finalServiceOrderService.updateFSO(resource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30602);
        }
    }

    @GetMapping(value = "/v1/{id}")
    public ResponseEntity<FinalServiceOrder> getFSOById(@PathVariable("id") String uniqueId) throws OperationException {
        try {
            return new ResponseEntity<>(finalServiceOrderService.getFSOById(uniqueId), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30603);
        }
    }

    @GetMapping(value = "/v1/getFSO")
    public ResponseEntity<ServiceOrderResource> getFSOResourceById(@RequestParam("id") String uniqueId) throws OperationException {
        try {
            return new ResponseEntity<>(finalServiceOrderService.getFSOResourceById(uniqueId), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30603);
        }
    }

    @PostMapping(value = "/v1/searchByCriteria")
    public ResponseEntity<Map<String, Object>> getFinalServiceOrders(@RequestBody ServiceOrderSearchCriteria criteria,
                                                                     @RequestParam(value = "systemUser", required = false) Boolean isSystemUser) throws OperationException, IOException, JSONException {
        try {
            if(isSystemUser==null)
                isSystemUser=false;
            return new ResponseEntity<>(finalServiceOrderService.getFinalServiceOrders(criteria, !isSystemUser), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30604);
        }
    }

    @PostMapping(value = "/v1/searchByIdAndVersion")
    public ResponseEntity<FinalServiceOrder> getFinalServiceOrder(@RequestBody VersionId resource) throws OperationException {
        try {
            return new ResponseEntity<>(finalServiceOrderService.getFSOByVersionId(resource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30605);
        }
    }
}
