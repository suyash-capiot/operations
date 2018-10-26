package com.coxandkings.travel.operations.controller.reconfirmation;

import com.coxandkings.travel.operations.enums.reconfirmation.ResponseStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationFilter;
import com.coxandkings.travel.operations.repository.reconfirmation.supplier.SupplierReconfirmationRepository;
import com.coxandkings.travel.operations.resource.outbound.be.ReconfirmationRequestResource;
import com.coxandkings.travel.operations.resource.reconfirmation.response.reconfirmation.SupplierReconfirmationResponse;
import com.coxandkings.travel.operations.resource.reconfirmation.supplier.SupplierCancelReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.supplier.SupplierReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.supplier.SupplierSendReconfirmationResource;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ClientReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmAttributeType;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationConfiguration;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("supplier/service-provider/")
public class SupplierReconfirmationController {

    @Autowired
    private ReconfirmationService reconfirmationService;

    @Autowired
    private SupplierReconfirmationService supplierReconfirmationService;

    @Autowired
    private ReconfirmationUtilityService reconfirmationUtilityService;

    @Autowired
    private ClientReconfirmationService clientReconfirmationService;

    @Autowired
    private ReconfirmationMDMService reconfirmationMDMService;


    @Autowired
    private SupplierReconfirmationRepository supplierReconfirmationRepository;

    @Autowired
    private RestUtils restUtils;

    @Value(value = "${reconfirmation.supplier.be.update_reconfirmation_date}")
    private String updateReconfirmationDateInBE;

    /**
     * @param reconfirmationRequests
     * @return
     * @throws OperationException
     */
    @PostMapping("/v1/updateSupplierReconfirmationDate")
    private ResponseEntity updateSplrReconfirmationDate(@RequestBody ReconfirmationRequestResource reconfirmationRequests) throws OperationException {
        return supplierReconfirmationService.updateSupplierReconfirmationDate(reconfirmationRequests);
    }

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    @PostMapping(value = "/v1/submit")
    public ResponseEntity<?> submit(@RequestBody SupplierReconfirmationResource request) throws OperationException {

        supplierReconfirmationService.validateSupplierReconfirmationId(request.getSupplierReconfirmationID());
        SupplierReconfirmationDetails supplierReconfirmationDetails = supplierReconfirmationService.findByReconfirmationID(request.getSupplierReconfirmationID());

        OpsBooking aBooking = this.reconfirmationService.getBookingById(supplierReconfirmationDetails.getBookRefNo());
        OpsProduct aProductDetails = reconfirmationService.getProductById(aBooking, supplierReconfirmationDetails.getOrderID());

        //added new condition
        SupplierReconfirmationFilter filter = new SupplierReconfirmationFilter();
        // filter.setSupplierId(aProductDetails.getSupplierID() != null ? aProductDetails.getSupplierID() : null);
        filter.setProductCategory(aProductDetails.getProductCategory() != null ? aProductDetails.getProductCategory() : null);
        filter.setProductCatSubtype(aProductDetails.getProductSubCategory() != null ? aProductDetails.getProductSubCategory() : null);
        ReconfirmationConfiguration configuration = reconfirmationMDMService.getConfiguration(filter);
        if (configuration == null) {
            throw new OperationException(Constants.ER336);
        }
        if (configuration.getConfigFor() == null) {
            throw new OperationException(Constants.ER336);
        }

        supplierReconfirmationDetails = this.supplierReconfirmationService.convertToSupplierReconfirmation(supplierReconfirmationDetails, request);
        if (supplierReconfirmationDetails != null) {
            supplierReconfirmationDetails = supplierReconfirmationService.saveOrUpdateSupplierReconfirmation(supplierReconfirmationDetails);
            if (supplierReconfirmationDetails == null) {
                throw new OperationException(Constants.ER328);
            }
            SupplierReconfirmationResponse response = this.supplierReconfirmationService.convertTo(supplierReconfirmationDetails);
            return new ResponseEntity(response, OK);
        } else {
            throw new OperationException(Constants.ER328);
        }

    }

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    @PostMapping(value = "/v1/sendForReconfirmation")
    public ResponseEntity<?> sendForReconfirmation(@RequestBody SupplierSendReconfirmationResource request) throws OperationException {

        SupplierReconfirmationResponse response = this.supplierReconfirmationService.sendForReConfirmationToSupplier(request);
        if (response != null) {
            return new ResponseEntity(response, OK);
        } else {
            throw new OperationException(Constants.ER332);
        }

    }

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    @PostMapping(value = "/v1/cancelReconfirmation")
    public ResponseEntity<?> cancelReconfirmation(@RequestBody SupplierCancelReconfirmationResource request) throws OperationException {

        SupplierReconfirmationResponse response = supplierReconfirmationService.cancelReconfirmationToSupplier(request);
        if (response != null) {
            return new ResponseEntity(response, OK);
        } else {
            throw new OperationException(Constants.ER330);
        }

    }


    /**
     * @param productReconfirmationNo
     * @return
     * @throws OperationException
     */
    // BR 312
    @GetMapping(value = "/v1/setFlagAsReconfirmedBySupplierOrServiceProvider/{productReconfirmationNo}")
    public ResponseEntity<?> setFlag(@PathVariable String productReconfirmationNo) throws OperationException {
        try {
            SupplierReconfirmationResponse response = new SupplierReconfirmationResponse();
            return new ResponseEntity(response, OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30124);
        }
    }


    /**
     * @param supplierReconfirmationId
     * @return
     * @throws OperationException
     */
    //TODO
    @GetMapping(value = "/v1/cancelReconfirmation")
    public ResponseEntity<?> cancelReconfirmation(@RequestParam("supplierReconfirmationId") String supplierReconfirmationId) throws OperationException {

        SupplierReconfirmationDetails details = null;
        SupplierReconfirmationDetails supplierReconfirmationDetails = this.supplierReconfirmationService.checkSupplierResponse(ResponseStatus.REJECTED, supplierReconfirmationId, null);
        if (supplierReconfirmationDetails != null) {
            details = supplierReconfirmationService.updateSupplierReconfirmation(supplierReconfirmationDetails);
            return new ResponseEntity(details, OK);
        } else {
            throw new OperationException(Constants.ER332);
        }

    }

    /**
     * @param supplierReconfirmationId
     * @return
     * @throws OperationException
     */
    //TODO
    @GetMapping(value = "/v1/updateReconfirmationAsPending")
    public ResponseEntity<?> updateReconfirmationAsPending(@RequestParam("supplierReconfirmationId") String supplierReconfirmationId) throws OperationException {

        SupplierReconfirmationDetails details = null;
        SupplierReconfirmationDetails supplierReconfirmationDetails = this.supplierReconfirmationService.checkSupplierResponse(ResponseStatus.NO_RESPONSE, supplierReconfirmationId, null);
        if (supplierReconfirmationDetails != null) {
            details = supplierReconfirmationService.updateSupplierReconfirmation(supplierReconfirmationDetails);
            return new ResponseEntity(details, OK);
        } else {
            throw new OperationException(Constants.ER332);
        }

    }

    /**
     * @param bookRefNo
     * @param orderId
     * @return
     * @throws OperationException
     */
    @GetMapping(value = "/v1/getSupplierReconfirmation/{bookRefNo}/{orderId}")
    public ResponseEntity<?> getSupplierReconfirmation(@PathVariable("bookRefNo") String bookRefNo, @PathVariable("orderId") String orderId) throws OperationException {

        SupplierReconfirmationDetails supplierReconfDetails = this.supplierReconfirmationService.findByBookRefAndOrderNo(bookRefNo, orderId);
        if (supplierReconfDetails == null) {
            OpsBooking aBooking = this.reconfirmationService.getBookingById(bookRefNo);
            OpsProduct product = reconfirmationService.getProductById(aBooking, orderId);
            this.reconfirmationService.processBookingWithProduct(product, aBooking);
            try {
                supplierReconfDetails = this.supplierReconfirmationService.findByBookRefAndOrderNo(bookRefNo, orderId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            supplierReconfDetails = this.supplierReconfirmationService.findByBookRefAndOrderNo(bookRefNo, orderId);
        }
        if (supplierReconfDetails != null) {
            SupplierReconfirmationResponse reconfirmationResponse = supplierReconfirmationService.convertTo(supplierReconfDetails);
            return new ResponseEntity(reconfirmationResponse, OK);
        } else {
            throw new OperationException(Constants.ER356);
        }

    }


    @GetMapping(value = "/v1/getBookingAttributes")
    public ResponseEntity<?> getReconfirmationAttributes() throws OperationException {
        try {
            List<ReconfirmAttributeType> reconfirmationBookingAttributes = clientReconfirmationService.getReconfirmationBookingAttributes();
            if (reconfirmationBookingAttributes == null || reconfirmationBookingAttributes.size() == 0) {
                throw new OperationException(Constants.ER327);
            }
            return new ResponseEntity<>(reconfirmationBookingAttributes, OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30128);
        }
    }
    /** remove **/

    @GetMapping(value = "/v1/getAll")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity(this.supplierReconfirmationService.getAllSupplierReconfirmation(), OK);
    }
    /** remove **/
    @GetMapping(value = "/v1/dAll")
    public ResponseEntity<?> dAll() {
        try {
            supplierReconfirmationRepository.deleteAllReconfirmation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("Success", OK);
    }
    /** remove **/
    @GetMapping(value = "/v1/dById/{reconId}")
    public ResponseEntity<?> dummy(@PathVariable("reconId") String reconId) {

        try {
            supplierReconfirmationRepository.deleteReconfirmation(reconId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("Success", OK);
    }
    /** remove **/
    @GetMapping(value = "/v1/dummy/{bookRefNo}/{orderId}")
    public ResponseEntity<?> dummy(@PathVariable("bookRefNo") String bookRefNo, @PathVariable("orderId") String orderId) throws OperationException {
        SupplierReconfirmationDetails supplierReconfDetails = null;
        OpsBooking aBooking = this.reconfirmationService.getBookingById(bookRefNo);
        OpsProduct product = reconfirmationService.getProductById(aBooking, orderId);
        this.reconfirmationService.processBookingWithProduct(product, aBooking);
        try {
            supplierReconfDetails = this.supplierReconfirmationService.findByBookRefAndOrderNo(bookRefNo, orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (supplierReconfDetails != null) {
            SupplierReconfirmationResponse reconfirmationResponse = supplierReconfirmationService.convertTo(supplierReconfDetails);
            return new ResponseEntity(reconfirmationResponse, OK);
        } else {
            throw new OperationException(Constants.ER356);
        }

    }
    /** remove **/
    @GetMapping(value = "/v1/Remake")
    public void getAllRe() {
        List<SupplierReconfirmationDetails> allSupplierReconfirmation = this.supplierReconfirmationService.getAllSupplierReconfirmation();
        for (SupplierReconfirmationDetails details : allSupplierReconfirmation) {
            String bookRefNo = details.getBookRefNo();
            String orderID = details.getOrderID();
            try {
                ResponseEntity<?> dummy = dummy(bookRefNo, orderID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
