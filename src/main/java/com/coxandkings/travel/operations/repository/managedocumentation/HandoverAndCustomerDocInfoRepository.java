package com.coxandkings.travel.operations.repository.managedocumentation;

import com.coxandkings.travel.operations.criteria.managedocumentation.HandoverAndCustomerDocInfoSearchCriteria;
import com.coxandkings.travel.operations.model.managedocumentation.HandoverAndCustomerDocInfo;

import java.util.List;

public interface HandoverAndCustomerDocInfoRepository {

    public HandoverAndCustomerDocInfo saveDetails(HandoverAndCustomerDocInfo handoverAndCustomerDocInfo);

    public List<HandoverAndCustomerDocInfo> getAllDetails();

    public List<HandoverAndCustomerDocInfo> searchDetails(HandoverAndCustomerDocInfoSearchCriteria searchCriteria);

    public void deleteDetails(String id);

}
