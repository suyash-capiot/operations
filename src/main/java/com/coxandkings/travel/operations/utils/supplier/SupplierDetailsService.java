package com.coxandkings.travel.operations.utils.supplier;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONObject;

public interface SupplierDetailsService {

    CommunicationType getSupplierCommunicationTypeBySupplierId(String supplierID) throws OperationException;

    String getSupplierDetails(String supplierID) throws OperationException;

    JSONObject getSupplierCredentialDetails(String credId);

    CommunicationType getSupplierCommunicationType(String supplierDetails) throws OperationException;

}
