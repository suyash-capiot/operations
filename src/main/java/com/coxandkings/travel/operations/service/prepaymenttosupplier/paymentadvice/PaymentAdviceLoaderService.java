package com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.SupplierPaymentResource;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentAdviceLoaderService {

    public SupplierPaymentResource loadSupplierDetails(String bookingReferenceId, String orderId, String supplierReferenceId, String paymentAdviceNumber) throws OperationException;

    public List<PaymentAdvice> getAllPaymentAdvices(String bookingID, String orderID ,String supplierId) throws OperationException;

    public BigDecimal getNetPayableToSupplier(String bookingID, String orderID);

}
