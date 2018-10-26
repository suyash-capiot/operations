package com.coxandkings.travel.operations.controller.manageproductupdates;

import com.coxandkings.travel.operations.criteria.manageproductupdates.FlightUpdateSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageproductupdates.UpdatedFlightInfo;
import com.coxandkings.travel.operations.resource.manageproductupdates.*;
import com.coxandkings.travel.operations.service.manageproductupdates.ManageProductUpdatesService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/productUpdate")
public class ManageProductUpdateController {


    @Autowired
    private ManageProductUpdatesService manageProductUpdatesService;
    @Audited
    private UserService userService;

    @PostMapping("/v1/search")
    public ResponseEntity<FlightProductSearchResult> getByCriteria(@RequestBody FlightUpdateSearchCriteria criteria) throws OperationException {


        try {
            return new ResponseEntity<>(manageProductUpdatesService.getFlightProductsByCriteria(criteria), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_21000);
        }
    }

    @PostMapping("/v1/update")
    public ResponseEntity<Map<String, List<String>>> updateFlightDetails(@RequestBody List<ProductUpdateFlightResource> resourceList) throws OperationException {
        return new ResponseEntity<>(manageProductUpdatesService.updateFlightInfo(resourceList), HttpStatus.OK);
    }

    @PostMapping("/v1/save")
    public ResponseEntity<List<UpdatedFlightInfo>> saveUpdatedFlightInfo(@RequestBody List<ProductUpdateFlightResource> resourceList) {
        return new ResponseEntity<>(manageProductUpdatesService.saveUpdatedFlightInfo(resourceList), HttpStatus.OK);
    }

    @PostMapping("/v1/getUpdatedFlightInfo")
    public ResponseEntity<Map<String, Object>> getUpdatedFlightInfo(@RequestBody PaginationDetails paginationDetails) {
        return new ResponseEntity<>(manageProductUpdatesService.getUpdatedFlightInfo(paginationDetails.getSize(), paginationDetails.getPage()), HttpStatus.OK);
    }

    @PostMapping("/v1/sendEmail")
    public ResponseEntity<Map<String, List<String>>> sendEmailToClientOrCustomer(@RequestBody List<CustomerOrClientEmailResource> emailResourceListt) throws OperationException {
        try {
            return new ResponseEntity<>(manageProductUpdatesService.sendEmailToClientOrCustomer(emailResourceListt), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_21003);
        }
    }

    @PostMapping("/v1/eTicket")
    public ResponseEntity<Map<String, List<String>>> generateEticket(@RequestBody List<EticketResource> eticketResource) throws OperationException {
        try {
            return new ResponseEntity<>(manageProductUpdatesService.generateEticket(eticketResource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException("Error occur while generating e-ticket");
        }
    }
}
