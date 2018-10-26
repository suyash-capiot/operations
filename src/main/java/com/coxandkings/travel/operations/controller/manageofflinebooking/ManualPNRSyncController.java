package com.coxandkings.travel.operations.controller.manageofflinebooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.resource.manageofflinebooking.ManualPNRSyncResource;
import com.coxandkings.travel.operations.resource.manageofflinebooking.OpsResponse;
import com.coxandkings.travel.operations.resource.manageofflinebooking.OrderDetails;
import com.coxandkings.travel.operations.resource.manageofflinebooking.ProductLevelActions;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualpnrsync.ManualPNRSyncService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(value = "offlineBooking")
@CrossOrigin(origins = "*")
public class ManualPNRSyncController
{
    @Autowired
    private ManualPNRSyncService manualPNRSyncService;
    @Autowired
    private MessageSource messageSource;

    /*@PostMapping(value = "/v1/retrieve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorResponseResource retrieveBooking(@Valid @RequestBody ManualPNRSyncResource manualPNRSyncResource, Errors errors) throws OperationException
    {
        if (null != errors && errors.hasErrors())
        {
            StringBuilder msgToUserBuilder = new StringBuilder();
            for (ObjectError error : errors.getAllErrors())
            {
                msgToUserBuilder.append(error.getDefaultMessage());
            }
            return manualPNRSyncService.getMessageToUser(msgToUserBuilder.toString(), HttpStatus.BAD_REQUEST);
        }

        if (manualPNRSyncService.retrieveBooking(manualPNRSyncResource))
        {
            //Need to add constants
            return manualPNRSyncService.getMessageToUser(messageSource.getMessage(Constants.COMPANY_SUPPLIER_CHARGES_AMEND_SUCCESS, new Object[0], Locale.US), HttpStatus.OK);
        }
        else
        {
            return manualPNRSyncService.getMessageToUser(messageSource.getMessage(Constants.TECHNICAL_ISSUE_SAVE, new Object[0], Locale.US), HttpStatus.BAD_REQUEST);
        }
    }*/


    @PostMapping(value = "/v1/retrieve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody OpsResponse retrieveBooking(@Valid @RequestBody ManualPNRSyncResource manualPNRSyncResource, Errors errors) throws OperationException
    {
        if (null != errors && errors.hasErrors())
        {
            StringBuilder msgToUserBuilder = new StringBuilder();
            for (ObjectError error : errors.getAllErrors())
            {
                msgToUserBuilder.append(error.getDefaultMessage());
            }

            Map<String, String> entity = new HashMap<>();
            entity.put("Error", String.format("Mandatory details are not entered :" + msgToUserBuilder.toString()));
            throw new OperationException(entity);
        }
        return manualPNRSyncService.retrieveBooking(manualPNRSyncResource);
    }
}