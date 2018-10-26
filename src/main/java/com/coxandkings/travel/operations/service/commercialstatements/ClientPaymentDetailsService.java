package com.coxandkings.travel.operations.service.commercialstatements;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentDetail;

public interface ClientPaymentDetailsService {

    ClientPaymentDetail createPaymentDetail(ClientPaymentDetail clientPaymentDetail) throws OperationException;

//    String updatePaymentDetail(ClientPaymentDetail clientPaymentDetail ,String id) throws OperationException;
    String updatePaymentDetail(ClientPaymentDetail clientPaymentDetail) throws OperationException;

    ClientPaymentDetail getClientPaymentDetailById(String id) throws OperationException;

    ClientPaymentDetail getPaymentDetails(String paymentAdviceNumber) throws OperationException;
}
