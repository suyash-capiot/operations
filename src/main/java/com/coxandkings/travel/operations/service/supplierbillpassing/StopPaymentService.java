package com.coxandkings.travel.operations.service.supplierbillpassing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.supplierbillpassing.StopPaymentResource;

import java.text.ParseException;
import java.util.Map;

public interface StopPaymentService {
     Map save(StopPaymentResource stopPaymentResource) throws OperationException, ParseException;
     Map release(StopPaymentResource stopPaymentResource) throws OperationException, ParseException;
     void releasePaymentScheduler();
}
