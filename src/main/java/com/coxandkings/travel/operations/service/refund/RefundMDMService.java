package com.coxandkings.travel.operations.service.refund;

import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.RefundConfiguration;

public interface RefundMDMService {
    String getB2BClient(String clientId) throws OperationException;

    String getB2CClient(String clientId) throws OperationException;

    String getB2BClientEmail(String clientID) throws OperationException;

    String getB2CClientEmail(String clientID) throws OperationException;

    String[] getClientGroup(String groupId) throws OperationException;

    RefundTypes getRefundConfiguration(RefundConfiguration refundConfiguration) throws OperationException;

    String getMarketName(String id) throws OperationException;
}
