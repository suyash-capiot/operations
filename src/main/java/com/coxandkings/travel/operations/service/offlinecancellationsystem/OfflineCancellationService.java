package com.coxandkings.travel.operations.service.offlinecancellationsystem;

import com.coxandkings.travel.operations.criteria.offlinecancellationsystem.OfflineCancellationCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.offlinecancellationsystem.OfflineCancellationRespon;
import com.coxandkings.travel.operations.resource.offlinecancellationsystem.OfflineCancellationSupplierMarkets;

import java.io.IOException;

public interface OfflineCancellationService {

    OfflineCancellationRespon doCancellation(OfflineCancellationCriteria offlineCancellationCriteria) throws OperationException,IOException, IllegalAccessException;

    OfflineCancellationSupplierMarkets getSupplierMarketsList(/*OfflineCancellationCriteria offlineCancellationCriteria*/String suuplierId) throws IllegalAccessException;

    public OfflineCancellationRespon doAmendment(OfflineCancellationCriteria offlineCancellationCriteria) throws OperationException,IOException, IllegalAccessException;
}
