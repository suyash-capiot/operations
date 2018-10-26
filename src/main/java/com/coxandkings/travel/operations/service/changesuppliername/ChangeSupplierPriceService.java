package com.coxandkings.travel.operations.service.changesuppliername;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.changesuppliername.DiscountOnSupplierPrice;
import com.coxandkings.travel.operations.model.changesuppliername.SupplementOnSupplierPrice;
import com.coxandkings.travel.operations.resource.changesuppliername.ChangeSupplierPriceApprovalResource;
import org.json.JSONObject;

public interface ChangeSupplierPriceService {
    JSONObject approveOrReject(ChangeSupplierPriceApprovalResource approvalResource) throws OperationException;

    JSONObject approveClientRequest(String id) throws OperationException;

    JSONObject rejectClientRequest(String identifier) throws OperationException;

    SupplementOnSupplierPrice getSupplement(String identifier) throws OperationException;

    DiscountOnSupplierPrice getDiscount(String identifier) throws OperationException;
}
