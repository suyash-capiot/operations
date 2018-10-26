package com.coxandkings.travel.operations.repository.refund;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.ChangeType;

public interface ChangeTypeRefundRepository {

    ChangeType saveAndUpdateChangeType(ChangeType changeType) throws OperationException;


    ChangeType getChangeTypeByClaimNo(String claimNo);
}
