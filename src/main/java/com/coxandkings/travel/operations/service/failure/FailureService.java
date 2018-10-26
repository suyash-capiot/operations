package com.coxandkings.travel.operations.service.failure;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.failure.FailureDetails;
import com.coxandkings.travel.operations.resource.booking.AssignStaffResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.failure.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface FailureService {

    List<FailureDetailsResource> getFailures(FailureSearchCriteria failureSearchCriteria) throws OperationException;

    /* void processBooking(KafkaBookingMessage failedBooking) throws OperationException;*/

    FailureDetails get(String id);

    void actionBookingFailure(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException;

    void actionPaymentFailure(OpsBooking opsBooking) throws OperationException;

    List<String> checkDuplicates(String bookID) throws OperationException;

    List<FailureDetailsResource> getDuplicates(DuplicateBookingsResource duplicateBookingsResource) throws OperationException;

    JSONArray checkSupplierSystem(OpsBooking opsBooking, OpsProduct product);

    Map updatePnr(PnrResource pnrResource) throws OperationException;

    Map updateSupplierRefNumber(SupplierRefResource supplierRefResource) throws OperationException;

    Map<String, String> updatePaymentDetails(PaymentInfoResource paymentInfoResource) throws OperationException;

    Map cancel(String bookID) throws OperationException;

    Map<String, String> searchToBook(String bookID, String orderID) throws OperationException;

    String reportToTechnical(String bookID) throws OperationException;

    EmailResponse sendCommunication(String bookID) throws OperationException;

    FailureDetailsResource convert(String bookID) throws OperationException;

    Integer getCommunicationCount(String bookID) throws OperationException;

    JSONObject getRequestHeader(OpsBooking booking, Boolean transaction) throws JSONException;

    JSONObject getUpdateAttributeRequest(OpsBooking opsBooking, OpsProduct opsProduct);

    void removeFailureFlagFromOrder(OpsBooking opsBooking, OpsProduct opsProduct);

    JSONArray getDocumentByOrder(String orderID, String productSubCategory);

    JSONObject getDocumentsBypax(JSONArray docDetails, String paxID);

    String startCancellationProcess(String bookingId) throws OperationException;

    String assignTo(AssignStaffResource assignStaffResource)throws OperationException;

}
