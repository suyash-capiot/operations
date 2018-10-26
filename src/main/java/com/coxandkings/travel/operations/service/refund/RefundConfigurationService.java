package com.coxandkings.travel.operations.service.refund;

import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.RefundConfiguration;
import com.coxandkings.travel.operations.resource.refund.RefundConfigurationResource;

import java.util.List;

public interface RefundConfigurationService {
    RefundConfiguration saveAndUpdate(RefundConfiguration refundConfiguration) throws OperationException;

    List<RefundConfiguration> findByCriteria(RefundConfiguration refundConfiguration);

    RefundTypes getRefundConfiguration(RefundConfiguration refundConfiguration);
}
