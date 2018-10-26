package com.coxandkings.travel.operations.controller.reconfirmation;

import com.coxandkings.travel.operations.enums.reconfirmation.ResponseStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ClientReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationUtilityService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@RestController
@CrossOrigin(value = "*")
@RequestMapping("clientResponse/operations/reconfirmation/clientResponse")
public class ClientResponseController {

    @Autowired
    private ReconfirmationService reconfirmationService;

    @Autowired
    private ClientReconfirmationService clientReconfirmationService;

    @Autowired
    private ReconfirmationUtilityService reconfirmationUtilityService;

    /**
     * @param ID
     * @return
     * @throws OperationException
     */
    @GetMapping(value = "/v1/accept")
    public ResponseEntity<?> acceptReconfirmation(@RequestParam("ID") String ID) throws OperationException {

        try {
            ClientReconfirmationDetails clientReconfirmationDetails = null;
            try {
                clientReconfirmationDetails = this.clientReconfirmationService.checkClientResponse(ResponseStatus.ACCEPTED, ID, null);
            } catch (OperationException e) {
                e.printStackTrace();
                String message = this.reconfirmationUtilityService.getMessage(e.getErrorCode(), Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "blue"), BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER387, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "blue"), BAD_REQUEST);
            }
            if (clientReconfirmationDetails != null) {
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER380, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "green"), OK);
            } else {
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER359, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "red"), BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30117);
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
            ClientReconfirmationDetails details = null;
            ClientReconfirmationDetails clientReconfirmationDetails = null;
            try {
                clientReconfirmationDetails = this.clientReconfirmationService.checkClientResponse(ResponseStatus.REJECTED, ID, null);
            } catch (OperationException e) {
                e.printStackTrace();
                String message = this.reconfirmationUtilityService.getMessage(e.getErrorCode(), Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "blue"), BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER387, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "blue"), BAD_REQUEST);
            }
            if (clientReconfirmationDetails != null) {
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER381, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "green"), OK);
            } else {
                String message = this.reconfirmationUtilityService.getMessage(Constants.ER359, Locale.ENGLISH);
                return new ResponseEntity(reconfirmationUtilityService.convertToHtml(message, "red"), BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30118);
        }
    }

    /**
     * @param clientReconfirmationId
     * @param reconfirmationOnHoldDate
     * @return
     * @throws OperationException
     */
    @GetMapping(value = "/v1/onHoldReconfirmation")
    public ResponseEntity<?> onHoldReconfirmation(@RequestParam("clientReconfirmationId") String clientReconfirmationId,
                                                  @RequestParam("reconfirmationOnHoldDate") String reconfirmationOnHoldDate) throws OperationException {
        try {
            ClientReconfirmationDetails details = null;
            ClientReconfirmationDetails clientReconfirmationDetails = this.clientReconfirmationService.checkClientResponse(ResponseStatus.ON_HOLD,
                    clientReconfirmationId, reconfirmationOnHoldDate != null ? DateTimeUtil.formatBEDateTimeZone(reconfirmationOnHoldDate) : null);
            if (clientReconfirmationDetails != null) {
                details = clientReconfirmationService.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
                return new ResponseEntity(details, OK);
            } else {
                throw new OperationException(Constants.ER359);
            }
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30119);
        }
    }


}





