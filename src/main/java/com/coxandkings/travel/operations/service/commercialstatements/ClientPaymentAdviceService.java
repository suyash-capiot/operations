package com.coxandkings.travel.operations.service.commercialstatements;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;


public interface ClientPaymentAdviceService {
    ClientPaymentAdvice savePaymentAdvice(ClientPaymentAdvice clientPaymentAdvice) throws OperationException;

    ClientPaymentAdvice updatePaymentAdvice(ClientPaymentAdvice clientPaymentAdvice);

    ClientPaymentAdvice getById(String id) throws OperationException;

    ClientPaymentAdvice getByPaymentAdviceNumber(String paymentAdviceNumber);

    ClientPaymentAdvice approvePaymentAdvise(String paymentAdviceNumber, String remarks) throws OperationException;

    ClientPaymentAdvice rejectPaymentAdvice(String paymentAdviceNumber, String remarks) throws OperationException;

    ClientPaymentAdvice searchClientPayment(PaymentCriteria paymentCriteria) throws  OperationException;
}
