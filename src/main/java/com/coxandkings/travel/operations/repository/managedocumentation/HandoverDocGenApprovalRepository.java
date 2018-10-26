package com.coxandkings.travel.operations.repository.managedocumentation;

import com.coxandkings.travel.operations.criteria.managedocumentation.HandoverDocGenSearchCriteria;
import com.coxandkings.travel.operations.model.managedocumentation.HandoverDocGenApprovalDetails;

import java.util.List;

public interface HandoverDocGenApprovalRepository {

    public HandoverDocGenApprovalDetails saveDetails(HandoverDocGenApprovalDetails handoverDocGenApprovalDetails);

    public HandoverDocGenApprovalDetails getDetailsById(String id);

    public List<HandoverDocGenApprovalDetails> searchDetails(HandoverDocGenSearchCriteria searchCriteria);

    public void deleteRecordById(String id);

}
