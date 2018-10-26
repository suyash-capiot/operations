package com.coxandkings.travel.operations.service.doTicketing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.doTicketing.DoTicketing;
import com.coxandkings.travel.operations.resource.doTicketing.ApproveRejectResource;
import com.coxandkings.travel.operations.resource.doTicketing.DoTicketingResource;
import com.coxandkings.travel.operations.resource.doTicketing.SendForApprovalResource;
import org.json.JSONArray;
import org.json.JSONObject;

public interface DoTicketingService {

    JSONObject repriceForAir(String bookId, String orderId) throws OperationException;

    JSONObject repriceAndBook(String bookId, String orderId) throws OperationException;

    DoTicketingResource retainOrRefund(DoTicketingResource doTicketingResource) throws OperationException;

    JSONObject issueTicketForAir(DoTicketingResource doTicketingResource) throws OperationException;

    JSONObject issueTicketForAir(String bookId, String orderId) throws OperationException;

    void issueTicketValidator(String bookId, String orderId) throws OperationException;

    DoTicketingResource sendForApproval(SendForApprovalResource sendForApprovalResource) throws OperationException;

    DoTicketingResource approveOrReject(ApproveRejectResource approveRejectResource) throws OperationException;

    DoTicketingResource getDoTicketing(String bookId, String orderId) throws OperationException;

    DoTicketingResource getDoTicketing(String id);

    DoTicketingResource repriceAndCalculateDifference(DoTicketingResource doTicketingResource) throws OperationException;

    DoTicketing repriceAndIssueTicket(DoTicketingResource doTicketingResource) throws OperationException;

    JSONArray getSupplierCredentials(String bookId, String orderId) throws OperationException;
}
