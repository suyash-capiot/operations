package com.coxandkings.travel.operations.repository.commercialstatements;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentDetail;

public interface ClientPaymentDetailsRepo {

    ClientPaymentDetail createPaymentDetail(ClientPaymentDetail clientPaymentDetail) throws OperationException;
    ClientPaymentDetail find(String id)throws OperationException;
//    String updateClientPaymentDetail(ClientPaymentDetail previousClientPaymentDetail, ClientPaymentDetail newClientPaymentDetail) throws OperationException;
    String updateClientPaymentDetail(ClientPaymentDetail newClientPaymentDetail) throws OperationException;

    ClientPaymentDetail serachPaymentDetails(String paymentAdviceNumber);
}
