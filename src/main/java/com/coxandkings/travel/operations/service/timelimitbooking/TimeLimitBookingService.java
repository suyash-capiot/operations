package com.coxandkings.travel.operations.service.timelimitbooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.timelimitbooking.TimeLimitExpiryResource;
import com.coxandkings.travel.operations.resource.timelimitbooking.convert.ClientBackgroundInfoCriteriaResource;
import com.coxandkings.travel.operations.resource.timelimitbooking.convert.TotalRevenueAndGrossProfitResource;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Map;

public interface TimeLimitBookingService {

    public String doExtensionCheck(String bookId, String clientId, String clientType, OpsProduct opsProduct) throws OperationException;

    public MessageResource convertToDefinite(String bookRefId, String bookID) throws OperationException;

    public ZonedDateTime calculateActualTimeLimitExpiry(OpsProduct opsProduct, String jsonBodyAsString) throws OperationException;

    public Map updateNewDate(TimeLimitExpiryResource timeLimitExpiryResource) throws ParseException, OperationException;

    public MessageResource changeApproverStatusToApproved(String referenceID) throws OperationException;

    public MessageResource changeApproverStatusToRejected(String referenceID);

    TotalRevenueAndGrossProfitResource getOneYearClientBackgroundInfo(String referenceID);

    TotalRevenueAndGrossProfitResource getClientBackgroundInfo(ClientBackgroundInfoCriteriaResource criteriaResource) throws OperationException;
}
