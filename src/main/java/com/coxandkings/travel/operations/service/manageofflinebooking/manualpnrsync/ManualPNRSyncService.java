package com.coxandkings.travel.operations.service.manageofflinebooking.manualpnrsync;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.resource.manageofflinebooking.ManualPNRSyncResource;
import com.coxandkings.travel.operations.resource.manageofflinebooking.OpsResponse;
import org.springframework.http.HttpStatus;

public interface ManualPNRSyncService
{
    OpsResponse retrieveBooking(ManualPNRSyncResource manualPNRSyncResource) throws OperationException;
    ErrorResponseResource getMessageToUser(String errorStr, HttpStatus httpStatus);
}
