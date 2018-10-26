package com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentdetails;

import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;

public interface PaymentDetailsRepository {

    PaymentDetails savePaymentDetails(PaymentDetails paymentDetails);

    PaymentDetails serachPaymentDetails(String paymentAdviceNumber);

    PaymentDetails getPaymentDetailsById(String id);
}
