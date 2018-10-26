package com.coxandkings.travel.operations.service.managedocumentation;

import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.exceptions.OperationException;

import java.util.Map;

public interface DocumentMDMService {
    String getCompanyName(String companyId) throws OperationException;

    String getSupplierName(String supplierId) throws OperationException;

    String supplierContact(String supplierId) throws OperationException;

    Map<String, String> getHotelDetails(String hotelName);

    String getClientName(String clientId, MDMClientType clientType) throws OperationException;
}
