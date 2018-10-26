package com.coxandkings.travel.operations.controller.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.FinalSupplierLiability;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.FinalSupplierLiabilityService;
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
@RequestMapping(value = "/finalSupplierLiabilities")
public class FinalSupplierLiabilityController {

    @Autowired
    private FinalSupplierLiabilityService finalSupplierLiabilityService;

    @PostMapping(value = "/v1/generate")
    public ResponseEntity<FinalSupplierLiability> generateFSL(@RequestBody ServiceOrderResource resource) throws OperationException, IOException, JSONException {
        try {
            return new ResponseEntity<>(finalSupplierLiabilityService.generateFSL(resource), HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30606);
        }
    }

    @PostMapping(value = "/v1/update")
    public ResponseEntity<FinalSupplierLiability> updateFSL(@RequestBody ServiceOrderResource resource) throws OperationException {
        try {
            return new ResponseEntity<>(finalSupplierLiabilityService.updateFSL(resource), HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30607);
        }
    }

    @GetMapping(value = "/v1/{id}")
    public ResponseEntity<FinalSupplierLiability> getFSLById(@PathVariable("id") String uniqueId) throws OperationException {
        try {
            return new ResponseEntity<>(finalSupplierLiabilityService.getFSLById(uniqueId), HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30608);
        }
    }

    @GetMapping(value = "/v1/getFSL")
    public ResponseEntity<ServiceOrderResource> getFSLResourceById(@RequestParam("id") String uniqueId) throws OperationException {
        try {
            return new ResponseEntity<>(finalSupplierLiabilityService.getFSLResourceById(uniqueId), HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30608);
        }
    }

    @PostMapping(value = "/v1/searchByCriteria")
    public ResponseEntity<Map<String, Object>> getFinalSupplierLiabilities(@RequestBody ServiceOrderSearchCriteria criteria) throws OperationException, IOException, JSONException {
        try {
            return new ResponseEntity<>(finalSupplierLiabilityService.getFinalSupplierLiabilities(criteria), HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30609);
        }
    }

    @PostMapping(value = "/v1/searchByIdAndVersion")
    public ResponseEntity<FinalSupplierLiability> getFinalSupplierLiability(@RequestBody VersionId resource) throws OperationException {
        try {
            return new ResponseEntity<>(finalSupplierLiabilityService.getFSLByVersionId(resource), HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30610);
        }
    }

}


