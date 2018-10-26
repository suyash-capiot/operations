package com.coxandkings.travel.operations.controller.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalSupplierLiability;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalSupplierLiabilityService;
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
@RequestMapping(value = "/provisionalSupplierLiabilities")
public class ProvisionalSupplierLiabilityController {

    @Autowired
    private ProvisionalSupplierLiabilityService provisionalSupplierLiabilityService;

    @PostMapping(value = "/v1/generate")
    public ResponseEntity<ProvisionalSupplierLiability> generatePSL(@RequestBody ServiceOrderResource resource) throws OperationException {
        try {
            return new ResponseEntity<>(provisionalSupplierLiabilityService.generatePSL(resource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30617);
        }
    }

    @PostMapping(value = "/v1/update")
    public ResponseEntity<ProvisionalSupplierLiability> updatePSL(@RequestBody ServiceOrderResource resource) throws OperationException {
        try {
            return new ResponseEntity<>(provisionalSupplierLiabilityService.updatePSL(resource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30618);
        }
    }

    @GetMapping(value = "/v1/{id}")
    public ResponseEntity<ProvisionalSupplierLiability> getPSLById(@PathVariable("id") String uniqueId) throws OperationException {
        try {
            return new ResponseEntity<>(provisionalSupplierLiabilityService.getPSLById(uniqueId), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30619);
        }
    }

    @GetMapping(value = "/v1/getPSL")
    public ResponseEntity<ServiceOrderResource> getPSLResourceById(@RequestParam("id") String uniqueId) throws OperationException {
        try {
            return new ResponseEntity<>(provisionalSupplierLiabilityService.getPSLResourceById(uniqueId), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30619);
        }
    }

    @PostMapping(value = "/v1/searchByCriteria")
    public ResponseEntity<Map<String, Object>> getProvisionalSupplierLiabilities(@RequestBody ServiceOrderSearchCriteria criteria) throws OperationException, IOException, JSONException {
        try {
            return new ResponseEntity<>(provisionalSupplierLiabilityService.getProvisionalSupplierLiabilities(criteria), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30620);
        }
    }

    @PostMapping(value = "/v1/searchByIdAndVersion")
    public ResponseEntity<ProvisionalSupplierLiability> gteProvisionalSupplierLiability(@RequestBody VersionId resource) throws OperationException {
        try {
            return new ResponseEntity<>(provisionalSupplierLiabilityService.getPSLByVersionId(resource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30621);
        }
    }

}
