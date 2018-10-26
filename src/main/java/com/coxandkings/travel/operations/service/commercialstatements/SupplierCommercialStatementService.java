package com.coxandkings.travel.operations.service.commercialstatements;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementSortingCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.SupplierCommercialStatement;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.commercialStatement.InvoiceCommercial;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SupplierCommercialStatementService {

    Map searchByCriteria(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;

    SupplierCommercialStatement get(String id) throws OperationException;

    Map updatePerformaInvoiceDetails(String statementid, String invoiceId) throws OperationException;

    Map updateFinalInvoiceDetails(String statementId, String invoiceId) throws OperationException;

    Map updateReceiptDetails(Set<String> statementIds, String receiptNumber) throws OperationException;

    Map updateCreditNoteDetails(String statementId, String creditNoteNumber) throws OperationException;

    SupplierCommercialStatement getByName(String statementName) throws OperationException;

    InvoiceCommercial getInvoiceDetails(String invoiceNumber) throws OperationException;

    SupplierCommercialStatement update(SupplierCommercialStatement supplierCommercialStatement);

    Map getBillPassingResource(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;

    Map getPaymentAdviceResource(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;

    Set<String> getSupplierNames();

    Set<String> getCommercialHeads();

    Set<String> getCompanyMarkets();

    Set<String> getCurrency();

    Set<String> getProductCategories();

    Set<String> getProductCategorySubTypes();

    Set<String> getProductNames();

    Set<String> getSettlementStatus();

    CommercialStatementSortingCriteria[] getSortingCriteria();

    void updatePaymentDetails(PaymentAdvice paymentAdvice);
}
