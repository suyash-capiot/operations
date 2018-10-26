package com.coxandkings.travel.operations.service.commercialstatements;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;
import com.coxandkings.travel.operations.model.commercialstatements.CommercialStatementsBillPassing;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementsBillPassingResource;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementsPaymentAdviceResource;

import java.util.List;
import java.util.Map;

public interface CommercialStatementsBillPassingService {

    Map updateStatus(String billPassingId, String status, String remarks) throws OperationException;

    CommercialStatementsBillPassingResource get(String id) throws OperationException;

    Map invoiceEntry(CommercialStatementsBillPassingResource commercialStatementsBillPassingResource) throws OperationException;

    List<String> getApprovalStatusList();

    Map generatePaymentAdvice(CommercialStatementsPaymentAdviceResource commercialStatementsPaymentAdviceResource) throws OperationException;

    Map getPaymentAdviceDetails(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;

    CommercialStatementsBillPassing update(CommercialStatementsBillPassing commercialStatementsBillPassing);

    Map getBillPassingResource(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;

    CommercialStatementsPaymentAdviceResource getPaymentAdviceByNumber(String paymentAdviceNumber,String commercialStatementFor) throws OperationException;

    ClientPaymentAdvice getPaymentAdviceById(String id) throws OperationException;
}
