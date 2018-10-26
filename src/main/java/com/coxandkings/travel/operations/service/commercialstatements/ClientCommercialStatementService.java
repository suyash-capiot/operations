package com.coxandkings.travel.operations.service.commercialstatements;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementSortingCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientCommercialStatement;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.commercialStatement.InvoiceCommercial;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ClientCommercialStatementService {

    ClientCommercialStatement create(ClientCommercialStatement clientCommercialStatement) throws OperationException;

    Map searchByCriteria(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;

    ClientCommercialStatement get(String id) throws OperationException;

    Map updatePerformaInvoiceDetails(String statementid, String invoiceId) throws OperationException;

    Map updateReceiptDetails(Set<String> statementIds, String claimNo) throws OperationException;

    Map updateCreditNoteDetails(String statementId, String creditNoteNumber) throws OperationException;

    Map updateFinalInvoiceDetails(String statementId, String invoiceId) throws OperationException;

    ClientCommercialStatement getByName(String statementName) throws OperationException;

    InvoiceCommercial getInvoiceDetails(String invoiceNumber);

    ClientCommercialStatement update(ClientCommercialStatement clientCommercialStatement);

    Map getBillPassingResource(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;

    Map getPaymentAdviceResource(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;

    Set<String> getCompanyMarkets();

    Set<String> getClientNames();

    Set<String> getCommercialHeads();

    Set<String> getCurrency();

    Set<String> getProductCategories();

    Set<String> getProductCategorySubTypes();

    Set<String> getProductNames();

    Set<String> getClientCategories();

    Set<String> getClientSubCategories();

    Set<String> getSettlementStatus();

    CommercialStatementSortingCriteria[] getSortingCriteria();

    void updatePaymentDetails(ClientPaymentAdvice clientPaymentAdvice);
}
