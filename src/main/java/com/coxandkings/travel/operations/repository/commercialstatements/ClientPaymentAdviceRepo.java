package com.coxandkings.travel.operations.repository.commercialstatements;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;

public interface ClientPaymentAdviceRepo {
     ClientPaymentAdvice add(ClientPaymentAdvice clientPaymentAdvice);
     ClientPaymentAdvice update(ClientPaymentAdvice clientPaymentAdvice);
     ClientPaymentAdvice getById(String id);

    ClientPaymentAdvice getByPaymentAdviceNumber(String payentAdviceNmber);

    ClientPaymentAdvice searchPaymentAdvise(PaymentCriteria paymentCriteria) throws OperationException;
}
