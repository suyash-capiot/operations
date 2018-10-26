package com.coxandkings.travel.operations.service.refund;

import com.coxandkings.travel.ext.model.finance.creditdebitnote.CreditDebitNote;
import com.coxandkings.travel.ext.model.finance.invoice.InvoiceDto;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.finaceRefund.RefundClaim;
import com.coxandkings.travel.operations.model.refund.finaceRefund.RefundProcess;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public interface RefundFinanceService {
    RefundClaim updateRefundClaim(RefundClaim refundClaim1) throws OperationException;

    RefundClaim getRefundClaim(String claimNo) throws OperationException;

    RefundClaim saveRefundClaim(RefundProcess refundProcess) throws OperationException;

    BigDecimal getMyAccountBalance(RefundClaim refundClaim) throws OperationException;

    List<RefundClaim> getRefundByBookingReferenceNo(String bookingReferenceNo, RestTemplate restTemplate) throws OperationException;

    Boolean isPromoUsed(String claimNo) throws OperationException;

    String getGetCreditNote(String creditNoteNo) throws OperationException;

    InvoiceDto getInvoicesByBookingDetails(String bookingNo, String orderId) throws OperationException;

    CreditDebitNote updateCreditNote(CreditDebitNote creditDebitNote) throws OperationException;

    CreditDebitNote createCreditDebitNote(RefundProcess refundProcess) throws OperationException;


}
