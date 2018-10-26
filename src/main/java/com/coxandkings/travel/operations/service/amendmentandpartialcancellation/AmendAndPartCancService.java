package com.coxandkings.travel.operations.service.amendmentandpartialcancellation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.resource.amendmentandpartialcancellation.AmendAndpartCancResource;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

public interface AmendAndPartCancService {
    boolean saveAmendAndPartCanc(AmendAndpartCancResource amendAndpartCancResource) throws OperationException;

    ErrorResponseResource approveRejectCompanyChargesAmendment(String requestJSON) throws OperationException;

    ErrorResponseResource getMessageToUser(String errorStr, HttpStatus httpStatus);

    ErrorResponseResource handleSupplierResponseToChargesAmendment(String suppResponse, String taskRefID) throws OperationException;

    JSONObject getAmendAndCancDetails(String taskRefId) throws OperationException;
}
