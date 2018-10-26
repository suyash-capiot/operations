package com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentadvice;


import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;

import java.util.List;


public interface PaymentAdviceRepository {

    public PaymentAdvice savePaymentAdvice(PaymentAdvice paymentAdvice);

    public PaymentAdvice updatePaymentAdvice(PaymentAdvice paymentAdvice);

    public List<PaymentAdvice> searchPaymentAdvise(PaymentCriteria paymentCriteria);

    public PaymentAdvice getById(String id);

    public String balancePayment(String bookingRefId, String orderId, String supplierId);

    public String netPayableAmount(String bookingRefId, String orderId, String supplierId);

    public String calculateAmountPayableToSupplier(String serviceOrderId);

}

