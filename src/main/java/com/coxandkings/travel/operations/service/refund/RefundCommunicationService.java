package com.coxandkings.travel.operations.service.refund;

import com.coxandkings.travel.operations.exceptions.OperationException;

public interface RefundCommunicationService {
    boolean sendMailToClient(String clientEmail, String clientName, String bookingNo, String orderNo);

    boolean sendMailToClient(String clientEmail, String clietName, String bookingNo, String orderNo, String creditNoteNo);


    void sendAlert(String refundClaimNo) throws OperationException;
    void sendApproverAlert(String refundClaimNo);
}
