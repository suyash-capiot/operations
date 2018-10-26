package com.coxandkings.travel.operations.repository.commercialstatements;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientCommercialStatement;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ClientCommercialStatementRepo {

    ClientCommercialStatement add(ClientCommercialStatement clientCommercialStatement);
    ClientCommercialStatement get(String id);
    Map searchByCriteria(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException;
    ClientCommercialStatement update(ClientCommercialStatement clientCommercialStatement);
    ClientCommercialStatement getByName(String statementName);
    Set<String> getCompanyMarkets();

    Set<String> getClientNames();

    Set<String> getCommercialHeads();

    Set<String> getCurrency();

    Set<String> getProductCategories();

    Set<String> getProductCategorySubTypes();

    Set<String> getProductNames();

    Set<String> getClientCategories();

    Set<String> getClientSubCategories();

    public List<ClientCommercialStatement> getAll(List<String> ids);

}
