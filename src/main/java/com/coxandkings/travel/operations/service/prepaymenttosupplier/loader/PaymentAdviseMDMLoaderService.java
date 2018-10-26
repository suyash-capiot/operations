package com.coxandkings.travel.operations.service.prepaymenttosupplier.loader;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.DateWisePaymentPercentage;
import org.apache.commons.codec.EncoderException;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

public interface PaymentAdviseMDMLoaderService {

    public List<String> getSupplierCurrenciesList(String supplierID) throws OperationException, EncoderException;

    public Map<String, Object> getSupplierSettlement(String supplierId) throws OperationException;

    public String getTypeOfSettlement(String supplierId,boolean uiTrigger) throws OperationException;

    public boolean getPrePaymentRequiredFlag(String supplierId) throws OperationException;

    public String getPaymentAdviceGenerationDueDate(String supplierId);

    public DateWisePaymentPercentage getPaymentDueDate(String bookingId, String orderId, String supplierId) throws JSONException, OperationException;

    public String getSupplierName(String supplierId) throws JSONException, OperationException;

    public String getModeOfPayment(String supplierId) throws JSONException, OperationException;

    public DateWisePaymentPercentage getAdvance(String bookingId, String orderId, String supplierId) throws OperationException;


}
