package com.coxandkings.travel.operations.service.specialrequest;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.MessageResource;
import org.json.JSONObject;

public interface SpecialRequestService {
    String getAllRequestTypes(String bookingId, String orderId) throws OperationException;

    MessageResource updateSSR(JSONObject updateSsrReqJson);
}
