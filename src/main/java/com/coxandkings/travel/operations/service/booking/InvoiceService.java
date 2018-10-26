package com.coxandkings.travel.operations.service.booking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONObject;

public interface InvoiceService {
    public JSONObject getLatestInvoice(String bookingNo) throws OperationException;
}
