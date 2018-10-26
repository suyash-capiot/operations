package com.coxandkings.travel.operations.controller.reconfirmation;

import com.coxandkings.travel.operations.enums.reconfirmation.ResponseStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ClientReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("supplierResponse/operations/reconfirmation/supplierResponse")
public class SupplierResponseController {

    @Autowired
    private ReconfirmationService reconfirmationService;

    @Autowired
    private SupplierReconfirmationService supplierReconfirmationService;

    @Autowired
    private ReconfirmationUtilityService reconfirmationUtilityService;

    @Autowired
    private ClientReconfirmationService clientReconfirmationService;

    /**
     * @param ID
     * @return
     * @throws OperationException
     */

    @GetMapping(value = "/v1/accept", produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> acceptReconfirmation(@RequestParam("ID") String ID) throws OperationException {
        try {
            SupplierReconfirmationDetails details = null;
            SupplierReconfirmationDetails supplierReconfirmationDetails = null;
            try {
                supplierReconfirmationDetails = this.supplierReconfirmationService.checkSupplierResponse(ResponseStatus.ACCEPTED, ID, null);
            } catch (OperationException e) {
                e.printStackTrace();
                String message = this.reconfirmationUtilityService.getMessage(e.getErrorCode(), Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "blue"), BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER387, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "blue"), BAD_REQUEST);
            }
            if (supplierReconfirmationDetails != null) {
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER380, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "green"), OK);
            } else {
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER332, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "red"), BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_3029);
        }
    }

    /**
     * @param ID
     * @return
     * @throws OperationException
     */
    @GetMapping(value = "/v1/reject")
    public ResponseEntity<?> rejectReconfirmation(@RequestParam("ID") String ID) throws OperationException {
        try {
            SupplierReconfirmationDetails details = null;
            SupplierReconfirmationDetails supplierReconfirmationDetails = null;
            try {
                supplierReconfirmationDetails = this.supplierReconfirmationService.checkSupplierResponse(ResponseStatus.REJECTED, ID, null);
            } catch (OperationException e) {
                e.printStackTrace();
                String message = this.reconfirmationUtilityService.getMessage(e.getErrorCode(), Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "blue"), BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER387, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "blue"), BAD_REQUEST);
            }
            if (supplierReconfirmationDetails != null) {
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER381, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "green"), OK);
            } else {
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER332, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "red"), BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_3030);
        }
    }

    /**
     * @param supplierReconfirmationId
     * @param reconfirmationOnHoldDate
     * @return
     * @throws OperationException
     */
    @GetMapping(value = "/v1/onHoldReconfirmation")
    public ResponseEntity<?> onHoldReconfirmation(@RequestParam("supplierReconfirmationId") String supplierReconfirmationId, @RequestParam("reconfirmationOnHoldDate") String reconfirmationOnHoldDate) throws OperationException {
        try {
            SupplierReconfirmationDetails details = null;
            SupplierReconfirmationDetails supplierReconfirmationDetails = this.supplierReconfirmationService.checkSupplierResponse(ResponseStatus.ON_HOLD,
                    supplierReconfirmationId, reconfirmationOnHoldDate != null ? DateTimeUtil.formatBEDateTimeZone(reconfirmationOnHoldDate) : null);
            if (supplierReconfirmationDetails != null) {
                details = supplierReconfirmationService.updateSupplierReconfirmation(supplierReconfirmationDetails);
                return new ResponseEntity(details, OK);
            } else {
                throw new OperationException(Constants.ER332);
            }
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_3031);
        }
    }

}
