package com.coxandkings.travel.operations.service.refund;

import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.ChangeType;
import com.coxandkings.travel.operations.model.refund.Refund;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ClientDetail;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ClientType;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.refund.ApprovalResource;
import com.coxandkings.travel.operations.resource.refund.ChangeRefundTypeResponse;
import com.coxandkings.travel.operations.resource.refund.RefundResource;
import com.coxandkings.travel.operations.resource.refund.SendMailResource;

import java.util.List;
import java.util.Map;

public interface RefundService {
    Refund add(RefundResource refundResource) throws OperationException;
    Refund update(RefundResource refundResource) throws OperationException;
    Refund get(String id) throws OperationException;
    List<Refund> refundClaimsByBookingReferenceNo(String bookingReferenceNo) throws OperationException;
    Map changeRefundType(ChangeRefundTypeResponse changeRefundTypeResponse) throws OperationException;
    Map approval(ApprovalResource approvalResource) throws OperationException;
    String getCreditNote(String crediNoteNo) throws OperationException;
    public ClientDetail getClientById(String clientId,String clientType) throws OperationException;

    MessageResource sendMailToClient(SendMailResource sendMailResource) throws OperationException;

    RefundTypes getDefaultRefundType(String clientId, ClientType clientType) throws OperationException;

    ChangeType getDefaultRefundType(String refundClaim);
}
