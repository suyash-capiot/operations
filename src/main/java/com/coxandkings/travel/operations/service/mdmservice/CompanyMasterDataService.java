package com.coxandkings.travel.operations.service.mdmservice;

import com.coxandkings.travel.operations.exceptions.OperationException;

import java.util.List;
import java.util.Map;

public interface CompanyMasterDataService {

    public Map<String,String> getCompanyNames(List<String> companyIDs ) throws OperationException;

    public String getCompanyDetails( String companyID ) throws OperationException;

    public String getDivisionDetails(String companyID, String buID) throws OperationException;

    String getCompanyMarketNameByID(String companyMarketId);
}
