package com.coxandkings.travel.operations.service.partPaymentMonitor;

import com.coxandkings.travel.operations.exceptions.OperationException;

import java.time.ZonedDateTime;

public interface PaymentDueDateService {
    ZonedDateTime calculatePaymentDueDate(String bookID) throws OperationException;
}
