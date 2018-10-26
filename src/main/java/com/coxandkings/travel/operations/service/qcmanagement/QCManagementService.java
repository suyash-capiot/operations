package com.coxandkings.travel.operations.service.qcmanagement;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.qcmanagement.QcStatusInfo;

import java.util.List;

public interface QCManagementService {
    public void qcCheck(OpsBooking opsBooking) throws OperationException;

    void qcCheckForAmendmentAndCancellation(OpsProduct opsProduct, String operation, String actionType) throws OperationException;
    public List<QcStatusInfo> getAllQcStatusInfo() throws OperationException;
}
