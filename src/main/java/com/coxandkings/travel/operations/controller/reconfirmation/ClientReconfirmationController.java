package com.coxandkings.travel.operations.controller.reconfirmation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationFilter;
import com.coxandkings.travel.operations.repository.reconfirmation.client.ClientReconfirmationRepository;
import com.coxandkings.travel.operations.resource.outbound.be.ClientReconfirmationRequestBE;
import com.coxandkings.travel.operations.resource.outbound.be.ReconfirmationRequestResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientCancelReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientRejectionResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientSendReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.response.reconfirmation.ClientReconfirmationResponse;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ClientReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmAttributeType;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationConfiguration;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("client/customer/")
public class ClientReconfirmationController {

    @Autowired
    private ReconfirmationService reconfirmationService;

    @Autowired
    private ClientReconfirmationService clientReconfirmationService;

    @Autowired
    private ReconfirmationUtilityService reconfirmationUtilityService;

    @Autowired
    private ReconfirmationMDMService reconfirmationMDMService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private ClientReconfirmationRepository clientReconfirmationRepository;


    private static Logger logger = LogManager.getLogger(ClientReconfirmationController.class);


    /**
     * @param reconfirmationRequests
     * @return
     * @throws OperationException
     */
    @PostMapping("/v1/updateClientReconfirmationDate")
    private ResponseEntity updateClientReconfirmationDate(@RequestBody ReconfirmationRequestResource reconfirmationRequests) throws OperationException {
        ClientReconfirmationRequestBE supplierReconfirmationRequestBE = new ClientReconfirmationRequestBE(reconfirmationRequests);
        try {
            Map<String, String> response = this.clientReconfirmationService.updateReconfirmationDateInBE(reconfirmationRequests);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("--Update Of Client Reconfirmation Date Failed--");
            throw new OperationException(Constants.OPS_ERR_30101);
        }
    }


    /**
     * @param request
     * @return
     * @throws OperationException
     */
    @PostMapping(value = "/v1/submit")
    public ResponseEntity<?> submit(@RequestBody ClientReconfirmationResource request) throws OperationException {
        clientReconfirmationService.validate(request.getClientReconfirmationID(), Constants.OPS_ERR_30110);
        ClientReconfirmationDetails clientReconfirmationDetails = clientReconfirmationService.findByReconfirmationID(request.getClientReconfirmationID());
        OpsBooking aBooking = this.reconfirmationService.getBookingById(clientReconfirmationDetails.getBookRefNo());
        OpsProduct aProductDetails = reconfirmationService.getProductById(aBooking, clientReconfirmationDetails.getOrderID());
        //added new condition
        SupplierReconfirmationFilter filter = new SupplierReconfirmationFilter();
        filter.setProductCategory(aProductDetails.getProductCategory() != null ? aProductDetails.getProductCategory() : null);
        filter.setProductCatSubtype(aProductDetails.getProductSubCategory() != null ? aProductDetails.getProductSubCategory() : null);
        ReconfirmationConfiguration configuration = reconfirmationMDMService.getConfiguration(filter);
        if (configuration == null) {
            throw new OperationException(Constants.OPS_ERR_30111);
        }
        if (configuration.getConfigFor() == null) {
            throw new OperationException(Constants.OPS_ERR_30111);
        }
        clientReconfirmationDetails = clientReconfirmationService.convertToClientReconfirmationDetails(clientReconfirmationDetails, request);
        if (clientReconfirmationDetails != null) {
            clientReconfirmationDetails = clientReconfirmationService.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
            if (clientReconfirmationDetails == null) {
                throw new OperationException(Constants.OPS_ERR_30113);
            }
            ClientReconfirmationResponse reconfirmationResponse = this.reconfirmationService.convertTo(clientReconfirmationDetails);
            return new ResponseEntity(reconfirmationResponse, OK);
        } else {
            throw new OperationException(Constants.OPS_ERR_30113);
        }

    }

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    @PostMapping(value = "/v1/sendForConfirmation")
    public ResponseEntity<?> sendForConfirmation(@RequestBody ClientSendReconfirmationResource request) throws OperationException {

        clientReconfirmationService.validate(request.getClientReconfirmationID(), Constants.OPS_ERR_30113);
        ClientReconfirmationResponse response = null;
        response = this.clientReconfirmationService.sendForReConfirmationToClient(request);
        if (response != null) {
            return new ResponseEntity(response, OK);
        } else {
            throw new OperationException(Constants.OPS_ERR_30116);
        }

    }

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    @PostMapping(value = "/v1/sendRejectionToClient")
    public ResponseEntity<?> sendRejectionToClient(@RequestBody ClientRejectionResource request) throws OperationException {

        clientReconfirmationService.validate(request.getClientReconfirmationID(), Constants.OPS_ERR_30113);
        clientReconfirmationService.validate(request.getRemarks(), Constants.OPS_ERR_30114);
        ClientReconfirmationResponse response = this.clientReconfirmationService.sendRejectionToClient(request);
        if (response != null) {
            return new ResponseEntity(response, OK);
        } else {
            throw new OperationException(Constants.OPS_ERR_30115);
        }

    }

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    @PostMapping(value = "/v1/cancelReconfirmation")
    public ResponseEntity<?> cancelReconfirmation(@RequestBody ClientCancelReconfirmationResource request) throws OperationException {

        clientReconfirmationService.validate(request.getClientReconfirmationID(), Constants.ER353);
        ClientReconfirmationResponse response = this.clientReconfirmationService.cancelReconfirmation(request);
        if (response != null) {
            return new ResponseEntity(response, OK);
        } else {
            throw new OperationException(Constants.ER357);
        }

    }


    /**
     * @param bookRefNo
     * @param orderId
     * @return
     * @throws OperationException
     */
    @GetMapping(value = "/v1/getClientReconfirmation/{bookRefNo}/{orderId}")
    public ResponseEntity<?> getClientReconfirmation(@PathVariable("bookRefNo") String bookRefNo, @PathVariable("orderId") String orderId) throws OperationException {

        ClientReconfirmationDetails clientReconfDetails = this.clientReconfirmationService.findByBookRefAndOrderNo(bookRefNo, orderId);
        if (clientReconfDetails == null) {
            OpsBooking aBooking = this.reconfirmationService.getBookingById(bookRefNo);
            OpsProduct product = reconfirmationService.getProductById(aBooking, orderId);
            this.reconfirmationService.processBookingWithProduct(product, aBooking);
            try {
                clientReconfDetails = this.clientReconfirmationService.findByBookRefAndOrderNo(bookRefNo, orderId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            clientReconfDetails = this.clientReconfirmationService.findByBookRefAndOrderNo(bookRefNo, orderId);
        }
        if (clientReconfDetails != null) {
            ClientReconfirmationResponse reconfirmationResponse = clientReconfirmationService.convertTo(clientReconfDetails);
            return new ResponseEntity(reconfirmationResponse, OK);
        } else {
            throw new OperationException(Constants.ER356);
        }

    }


    @GetMapping(value = "/v1/getBookingAttributes")
    public ResponseEntity<?> getReconfirmationAttributes() throws OperationException {

        List<ReconfirmAttributeType> reconfirmationBookingAttributes = clientReconfirmationService.getReconfirmationBookingAttributes();
        if (reconfirmationBookingAttributes == null || reconfirmationBookingAttributes.size() == 0) {
            throw new OperationException(Constants.ER352);
        }
        return new ResponseEntity<>(reconfirmationBookingAttributes, OK);

    }


    // : BR 335
    @GetMapping(value = "/v1/reconfirmationRequestThroughLink")
    public ResponseEntity<?> onClickRedirectToERP(@RequestParam("bookId") String bookId,
                                                  @RequestParam("productId") String productId, @RequestParam("productStatus") String productStatus) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        //   headers.setLocation(URI.create(this.reconfirmationUtilityService.getHyperLinkFor()));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

    }
    /** now use **/

    /**
     * @param bookRefNo
     * @return
     * @throws OperationException
     */
    @GetMapping(value = "/v1/manualKafkaWork/{bookRefNo}")
    public ResponseEntity<?> manualKafkaWork(@PathVariable("bookRefNo") String bookRefNo) throws OperationException {
        try {
            OpsBooking aBooking = null;
            try {
                aBooking = this.reconfirmationService.getBookingById(bookRefNo);
                this.reconfirmationService.processBooking(aBooking, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ResponseEntity(aBooking, OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30109);
        }
    }

    /** **/
    @GetMapping(value = "/v1/dummy/{bookRefNo}/{orderId}")
    public ResponseEntity<?> dummy(@PathVariable("bookRefNo") String bookRefNo, @PathVariable("orderId") String orderId) throws OperationException {

        ClientReconfirmationDetails clientReconfDetails = null;
        if (clientReconfDetails == null) {
            OpsBooking aBooking = this.reconfirmationService.getBookingById(bookRefNo);
            OpsProduct product = reconfirmationService.getProductById(aBooking, orderId);
            this.reconfirmationService.processBookingWithProduct(product, aBooking);
        } else {
            clientReconfDetails = this.clientReconfirmationService.findByBookRefAndOrderNo(bookRefNo, orderId);
        }
        if (clientReconfDetails != null) {
            ClientReconfirmationResponse reconfirmationResponse = clientReconfirmationService.convertTo(clientReconfDetails);
            return new ResponseEntity(reconfirmationResponse, OK);
        } else {
            throw new OperationException(Constants.ER356);
        }

    }

    /** remove **/


    @GetMapping(value = "/v1/getAll")
    public ResponseEntity<?> getAll() throws OperationException {
        return new ResponseEntity(this.reconfirmationService.getAllCCReconfirmation(), OK);

    }
    /*** remove ***/

    @GetMapping(value = "/v1/dAll")
    public ResponseEntity<?> dAll() throws OperationException {
        try {
            clientReconfirmationRepository.deleteAllReconfirmation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("Success", OK);

    }
    /*** remove**/
    @GetMapping(value = "/v1/dById/{reconId}")
    public ResponseEntity<?> dummy(@PathVariable("reconId") String reconId) {

        try {
            clientReconfirmationRepository.deleteReconfirmation(reconId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("Success", OK);
    }

    /*** remove ***/
    @GetMapping(value = "/v1/Remake")
    public void getAllRe() {
        List<ClientReconfirmationDetails> allSupplierReconfirmation = this.reconfirmationService.getAllCCReconfirmation();
        for (ClientReconfirmationDetails details : allSupplierReconfirmation) {
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
