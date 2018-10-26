package com.coxandkings.travel.operations.repository.commercialstatements;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.SupplierCommercialStatement;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SupplierCommercialStatementRepo {

    SupplierCommercialStatement add(SupplierCommercialStatement supplierCommercialStatement);

    SupplierCommercialStatement get(String id);

    Map searchByCriteria(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;

    SupplierCommercialStatement update(SupplierCommercialStatement supplierCommercialStatement);

    SupplierCommercialStatement getByName(String statementName);

    Set<SupplierCommercialStatement> getStatementsForPaymentAdvice(String paymentAdviceNumber);

    Set<String> getSupplierNames();

    Set<String> getCommercialHeads();

    Set<String> getCompanyMarkets();

    Set<String> getCurrency();

    Set<String> getProductCategories();

    Set<String> getProductCategorySubTypes();

    Set<String> getProductNames();

    public List<SupplierCommercialStatement> getAll(List<String> ids);

}

