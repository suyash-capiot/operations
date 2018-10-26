package com.coxandkings.travel.operations.repository.refund;

import com.coxandkings.travel.operations.model.refund.RefundData;

public interface RefundDataRepository {
    void saveOrUpdate(RefundData refundData);

    RefundData getRefund(String claimNo);
}
