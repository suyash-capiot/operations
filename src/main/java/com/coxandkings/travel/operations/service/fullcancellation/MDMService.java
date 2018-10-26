package com.coxandkings.travel.operations.service.fullcancellation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.fullcancellation.clientKPI.KpiDefinition;

public interface MDMService {
    public String getSupplierInfo(String supplierId) throws OperationException;

    String getClientEmail(String clientId) throws OperationException;

    String getClientName(String clientId) throws OperationException;

    KpiDefinition getKPIDate(String clientId) throws OperationException;
}
