package com.coxandkings.travel.operations.repository.amendmentandpartialcancellation;

import com.coxandkings.travel.operations.model.amendmentandpartialcancellation.AmendAndPartCanc;

import java.util.List;

public interface CompChgAmendAndPartCancRepository {
    boolean saveCompanyChargesAmendment(List<AmendAndPartCanc> amendAndPartCancList);

    int approveRejectCompanyChargesAmendment(String bookingRefId, String approvalStatus, String remark);

    List<AmendAndPartCanc> getAmendmentDetailsByTaskRefId(String taskRefID);

    int updateSupplierResponseToAmendment(String suppResponse, String taskRefID);

    List<AmendAndPartCanc> getSupplierChargesAmendments(String taskRefID);

    List<AmendAndPartCanc> getAmendAndCancDetailsList(String taskRefId);
}
