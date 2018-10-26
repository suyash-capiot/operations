package com.coxandkings.travel.operations.service.partPaymentMonitor;

import com.coxandkings.travel.operations.exceptions.OperationException;

import java.util.List;

public interface PartPaymentMonitorFinanceService {
    public List<String> getPartPaymentBookings() throws OperationException;
}
