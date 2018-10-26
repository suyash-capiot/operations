package com.coxandkings.travel.operations.controller.amendmentandpartialcancellation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.resource.amendmentandpartialcancellation.AmendAndpartCancResource;
import com.coxandkings.travel.operations.service.amendmentandpartialcancellation.AmendAndPartCancService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/amendments")
@CrossOrigin(origins = "*")
public class AmendmentAndPartialCancellationController {

    private static Logger logger = LogManager.getLogger(AmendmentAndPartialCancellationController.class);

    @Autowired
    private AmendAndPartCancService amendAndPartCancService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping(value = "/v1/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorResponseResource saveAmendAndPartCancDetails(@Valid @RequestBody AmendAndpartCancResource amendAndpartCancResource, Errors errors) throws OperationException {
        logger.info("-- Entering AmendmentAndPartialCancellationController.saveAmendAndPartCancDetails");

        try {
            if (null != errors && errors.hasErrors()) {
                StringBuilder msgToUserBuilder = new StringBuilder();
                for (ObjectError error : errors.getAllErrors()) {
                    msgToUserBuilder.append(error.getDefaultMessage());
                }
                logger.error("-- Field level validations : "+msgToUserBuilder.toString());
                throw new OperationException(msgToUserBuilder.toString());
            }
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10400);
        }

        if (amendAndPartCancService.saveAmendAndPartCanc(amendAndpartCancResource)) {
            try {
                return amendAndPartCancService.getMessageToUser(messageSource.getMessage(Constants.COMPANY_SUPPLIER_CHARGES_AMEND_SUCCESS, new Object[0], Locale.US), HttpStatus.OK);
            } catch (Exception e) {
                throw new OperationException(Constants.OPS_ERR_10401);
            }
        } else {

            try {
                return amendAndPartCancService.getMessageToUser(messageSource.getMessage(Constants.TECHNICAL_ISSUE_SAVE, new Object[0], Locale.US), HttpStatus.BAD_REQUEST);
            } catch (NoSuchMessageException e) {
                throw new OperationException(Constants.OPS_ERR_10401);
            }
        }
    }

    @PostMapping(value = "/v1/approveRejectCompanyChargesAmendment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorResponseResource approveRejectCompanyChargesAmendment(@RequestBody String requestJSON) throws OperationException {
        logger.info("-- Entering AmendmentAndPartialCancellationController.approveRejectCompanyChargesAmendment");
        try {
            logger.info("-- Exiting AmendmentAndPartialCancellationController.approveRejectCompanyChargesAmendment");
            return amendAndPartCancService.approveRejectCompanyChargesAmendment(requestJSON);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_10403);
        }
    }

    @GetMapping(value = "/v1/supplierResponse/{response}")
    public ErrorResponseResource handleSupplierResponseToChargesAmendment(@PathVariable(value = "response") String suppResponse, @RequestParam("id") String taskRefID) throws OperationException {
        logger.info("-- Entering AmendmentAndPartialCancellationController.handleSupplierResponseToChargesAmendment");
        try {
            logger.info("-- Exiting AmendmentAndPartialCancellationController.handleSupplierResponseToChargesAmendment");
            return amendAndPartCancService.handleSupplierResponseToChargesAmendment(suppResponse, taskRefID);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_10404);
        }
    }

    @GetMapping("/v1/{taskRefId}")
    public JSONObject getAmendAndCancDetails(@PathVariable("taskRefId") String taskRefId) throws OperationException {
        logger.info("-- Entering AmendmentAndPartialCancellationController.getAmendAndCancDetails");
        return amendAndPartCancService.getAmendAndCancDetails(taskRefId);
    }
}
