package com.coxandkings.travel.operations.service.accountsummary;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.helper.booking.payment.AccountSummary;
import com.coxandkings.travel.operations.model.accountsummary.*;
import com.coxandkings.travel.operations.model.core.OpsBooking;

import java.io.IOException;
import java.util.List;

public interface AccountSummaryService {

    public List<InvoiceBasicInfo> getInvoiceInfoForBooking( String bookingID ) throws OperationException;

    public List<InvoicePaymentInfo> getInvoicePaymentInfo(String invoiceID ) throws OperationException;

    public List<InvoiceCreditDebitNoteInfo> getCreditDebitNoteInfoForInvoice(String invoiceID ) throws OperationException;

    public List<InvoiceRefundInfo> getInvoiceRefundInfo( String bookingID, String invoiceID ) throws OperationException;

    public List<BookingPaymentAdviseInfo> loadPaymentAdvisesForBooking( OpsBooking aBooking ) throws OperationException, IOException;

    public void updateAccountSummaryForPayments(AccountSummary actSummary) throws OperationException;

    public AccountSummary getClientPaymentDetails(String bookingRefId) throws OperationException;

}
