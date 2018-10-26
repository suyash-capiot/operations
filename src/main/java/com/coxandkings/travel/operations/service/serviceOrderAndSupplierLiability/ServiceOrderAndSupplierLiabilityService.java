package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.BaseServiceOrderDetails;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.DuplicateBookingsInfoResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ServiceOrderAndSupplierLiabilityService {

    public Map<String, Object> getServiceOrdersAndSupplierLiabilities(ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException, IOException, JSONException;
    public Map<String, Object> getServiceOrdersAndSupplierLiabilities(ServiceOrderSearchCriteria serviceOrderSearchCriteria, Boolean checkForUserCompany) throws OperationException, IOException, JSONException;
    public Map<String,String> processSOSLForCancelledProduct(ServiceOrderResource resource) throws OperationException, IOException, JSONException;
    public void processBooking(OpsBooking aBooking, KafkaBookingMessage message) throws IllegalAccessException, ParseException, IOException, OperationException, InvocationTargetException, JSONException;
    public void generateServiceOrder(OpsProduct product, OpsBooking opsBooking, Boolean isAmended) throws IOException, OperationException;
    public void generateServiceOrder(OpsProduct product, String bookID, Boolean isAmended) throws IOException, OperationException;
    public String linkPaymentAdviceWithServiceOrder(PaymentDetails paymentDetails) throws IOException, OperationException;
    public List<String> getServiceOrderStatus();
    public List<String> getSupplierSettlementStatus();
    public void linkCreditOrDebitNoteWithFSL(ServiceOrderResource serviceOrderResource) throws IOException, OperationException;
    public void generateServiceOrderForAmendedProduct(OpsProduct product, OpsBooking opsBooking) throws IOException, OperationException;
    public void generateServiceOrderForCancelledProduct(OpsProduct product, String bookID) throws IOException, OperationException;
    public DuplicateBookingsInfoResource getDuplicateBookingsInfo(String id) throws OperationException;
    public String generatePSOForDuplicateBookings(ServiceOrderResource resource) throws OperationException;
    public void setCompanyDetails(BaseServiceOrderDetails psl) throws OperationException;

    JSONArray getAutoSuggestValues(JSONObject req);

    JSONArray getBookIdAutoSuggest(JSONObject req);

//    Map<String,Object> getServiceOrdersAndSupplierLiabilitiesWorkFlow(SupplierBillPassingSearchCriteria searchCriteria) throws OperationException;
}
